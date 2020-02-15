from rest_framework import routers
from django.urls import path

from .views import *


router = routers.DefaultRouter()
router.register(r'batting', BattingViewSet)
router.register(r'pitching', PitchingViewSet)
router.register(r'fielding', FieldingViewSet)
