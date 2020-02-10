from rest_framework import routers
from .views import BattingViewSet, PitchingViewSet


router = routers.DefaultRouter()
router.register(r'batting', BattingViewSet)
router.register(r'pitching', PitchingViewSet)
