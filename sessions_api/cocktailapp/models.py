from django.db import models


class CustomUser(models.Model):
    username=models.charField(unique=True, max_lengtn=50)
    ecrypted_password= models.CharField(max_length=100)
    email = models.CharField(unique=True, max_length=200)

    def __str__(self):
        return self.username


class UserSession(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    token = models.CharField(unique=True, max_length=20)

    def __str__(self):
        return str(self.user) + ' - ' + self.token


