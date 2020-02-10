from django.contrib import admin
from .models import Batting, Pitching


@admin.register(Batting)
class BattingAdmin(admin.ModelAdmin):
    pass


@admin.register(Pitching)
class PitchingAdmin(admin.ModelAdmin):
    pass
