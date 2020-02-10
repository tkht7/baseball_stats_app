from rest_framework import routers
from .views import BattingViewSet, PitchingViewSet, FieldingViewSet


router = routers.DefaultRouter()
router.register(r'batting', BattingViewSet)
router.register(r'pitching', PitchingViewSet)
router.register(r'fielding', FieldingViewSet)
