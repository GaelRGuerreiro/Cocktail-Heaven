import json
import secrets
import bcrypt
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import CustomUser, UserSession

SESSION_TOKEN_HEADER='Sesion-Token'


@csrf_exempt
def register(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)
    body = json.loads(request.body)
    new_username = body.get('username', None)
    if new_username == None:
        return JsonResponse({'error': 'Missing username in request body'}, status=400)
    new_email = body.get('email', None)
    if new_email == None:
        return JsonResponse({'error': 'Missing email in request body'}, status=400)
    try:
        CustomUser.objects.get(username=new_username)
        CustomUser.objects.get(email=new_email)
    except CustomUser.DoesNotExist:
        # Proceed
        new_password = body.get('password', None)
        if new_password == None:
            return JsonResponse({'error': 'Missing password in request body'}, status=400)

        encrypted_pass = bcrypt.hashpw(new_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        new_user = CustomUser()
        new_user.username = new_username
        new_user.email = new_email
        new_user.encrypted_password = encrypted_pass
        new_user.save()
        return JsonResponse({'created': 'True'}, status=201)

    # User DOES exist.
    return JsonResponse({'error': 'User with given username or email already exists'}, status=409)


@csrf_exempt
def login(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

    body = json.loads(request.body)
    username = body.get('username')
    if username is None:
        return JsonResponse({'error': 'Missing username in request body'}, status=400)

    try:
        user = CustomUser.objects.get(username=username)
    except CustomUser.DoesNotExist:
        return JsonResponse({'error': 'Username does not exist'}, status=404)

    password = body.get('password')
    if password is None:
        return JsonResponse({'error': 'Missing password in request body'}, status=400)

    # Comprobando si la contraseña es válida
    if bcrypt.checkpw(password.encode('utf8'), user.encrypted_password.encode('utf8')):
        new_session = UserSession()
        new_session.user = user
        new_session.token = secrets.token_hex(10)
        new_session.save()
        return JsonResponse({
            'created': 'True',
            'sessionId': new_session.id,
            'sessionToken': new_session.token
        }, status=201)
    else:
        return JsonResponse({'error': 'Password is invalid'}, status=401)


def __get_logged_user(request):
    session_token = request.headers.get(SESSION_TOKEN_HEADER, None)
    if session_token == None:
        return None
    try:
        session = UserSession.objects.get(token=session_token)
        return session.user
    except UserSession.DoesNotExist:
        return None

