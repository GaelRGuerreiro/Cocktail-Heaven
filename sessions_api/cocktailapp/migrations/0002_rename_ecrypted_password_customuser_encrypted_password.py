# Generated by Django 5.0.3 on 2024-10-04 08:32

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('cocktailapp', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='customuser',
            old_name='ecrypted_password',
            new_name='encrypted_password',
        ),
    ]
