from django.db import models


class CustomUser(models.Model):
    username=models.CharField(unique=True, max_length=50)
    encrypted_password= models.CharField(max_length=100)
    email = models.CharField(unique=True, max_length=200)

    def __str__(self):
        return self.username


class UserSession(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    token = models.CharField(unique=True, max_length=20)

    def __str__(self):
        return str(self.user) + ' - ' + self.token



class FavoriteCocktail(models.Model):
    user = models.ForeignKey(CustomUser, on_delete=models.CASCADE)
    cocktail_id = models.CharField(max_length=50)
    cocktail_name = models.CharField(max_length=500)
    cocktail_image_url = models.CharField(max_length=500)

    def __str__(self):
        return str(self.user)+'-'+ str(self.cocktail_name)