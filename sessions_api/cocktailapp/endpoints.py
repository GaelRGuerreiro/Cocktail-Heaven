import secrets
from .models import CustomUser, UserSession, FavoriteCocktail

SESSION_TOKEN_HEADER='Sesion-Token'

from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.db import IntegrityError
import json
import bcrypt

@csrf_exempt
def register(request):
    if request.method != 'POST':
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

    # Parseamos el cuerpo de la solicitud
    try:
        body = json.loads(request.body)
    except json.JSONDecodeError:
        return JsonResponse({'error': 'Invalid JSON body'}, status=400)

    # Validación de campos obligatorios
    new_username = body.get('username')
    new_email = body.get('email')
    new_password = body.get('password')

    if not new_username:
        return JsonResponse({'error': 'Missing username in request body'}, status=400)
    if not new_email:
        return JsonResponse({'error': 'Missing email in request body'}, status=400)
    if not new_password:
        return JsonResponse({'error': 'Missing password in request body'}, status=400)

    # Verifica si ya existe un usuario con el mismo username o email
    if CustomUser.objects.filter(username=new_username).exists():
        return JsonResponse({'error': 'Username already in use'}, status=409)
    if CustomUser.objects.filter(email=new_email).exists():
        return JsonResponse({'error': 'Email already in use'}, status=409)

    # Crear y guardar el usuario
    try:
        encrypted_pass = bcrypt.hashpw(new_password.encode('utf8'), bcrypt.gensalt()).decode('utf8')
        new_user = CustomUser(username=new_username, email=new_email, encrypted_password=encrypted_pass)
        new_user.save()
        return JsonResponse({'created': True}, status=201)
    except IntegrityError:
        return JsonResponse({'error': 'Failed to create user due to a database error'}, status=500)

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

@csrf_exempt
def mark_unmark_favorite(request, cocktail_id):
    user = __get_logged_user(request)
    if user is None:
        return JsonResponse({'error': 'User not authenticated'}, status=401)

    if request.method == 'GET':
        # Verificar si el cóctel está en favoritos
        is_favorite = FavoriteCocktail.objects.filter(user=user, cocktail_id=cocktail_id).exists()
        return JsonResponse({'favorite': is_favorite}, status=200)

    elif request.method == 'DELETE':
        # Eliminar un cóctel de favoritos
        try:
            favorite_cocktail = FavoriteCocktail.objects.get(user=user, cocktail_id=cocktail_id)
            favorite_cocktail.delete()
            return JsonResponse({'ok': 'Removed from favorites'}, status=200)
        except FavoriteCocktail.DoesNotExist:
            return JsonResponse({'error': 'Cocktail not found in favorites'}, status=404)

    elif request.method == 'PUT':
        # Añadir un cóctel a favoritos
        body = json.loads(request.body)
        cocktail_name = body.get('cocktail_name')
        cocktail_image_url = body.get('cocktail_image_url')

        if not all([cocktail_name, cocktail_image_url]):
            return JsonResponse({'error': 'Missing cocktail_name or cocktail_image_url in request body'}, status=400)

        # Intentar eliminar si ya existe, para evitar duplicados
        FavoriteCocktail.objects.filter(user=user, cocktail_id=cocktail_id).delete()

        new_fav = FavoriteCocktail(
            user=user,
            cocktail_id=cocktail_id,
            cocktail_name=cocktail_name,
            cocktail_image_url=cocktail_image_url
        )
        new_fav.save()
        return JsonResponse({'ok': 'Added to favorites'}, status=201)

    else:
        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)

def favorite_cocktails(request):
    user = __get_logged_user(request)
    if user is None:
        return JsonResponse({'error': 'User not authenticated'}, status=401)

    if request.method == 'GET':
        favorites = FavoriteCocktail.objects.filter(user=user)
        favorites_list = [
            {
                'cocktail_id': fav.cocktail_id,
                'cocktail_name': fav.cocktail_name,
                'cocktail_image_url': fav.cocktail_image_url
            } for fav in favorites
        ]
        return JsonResponse({
            'username': user.username,
            'email': user.email,
            'favorites': favorites_list
        }, status=200)

        return JsonResponse({'error': 'Unsupported HTTP method'}, status=405)