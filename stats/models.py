from django.db import models

class Result(models.Model):
    name = models.CharField(max_length=255, verbose_name='名前')
    b_num = models.IntegerField(verbose_name='打席')
    point = models.IntegerField(verbose_name='得点')    
    hit = models.IntegerField(verbose_name='安打')
    hit2 = models.IntegerField(verbose_name='二塁打')
    hit3 = models.IntegerField(verbose_name='三塁打')
    homerun = models.IntegerField(verbose_name='本塁打')
    b_point = models.IntegerField(verbose_name='打点')
    steal = models.IntegerField(verbose_name='盗塁')
    steal_out = models.IntegerField(verbose_name='盗塁刺')
    s_hit = models.IntegerField(verbose_name='犠打')
    s_fly = models.IntegerField(verbose_name='犠飛')
    fourball = models.IntegerField(verbose_name='四球')
    intent_four = models.IntegerField(verbose_name='故意四')
    deadball = models.IntegerField(verbose_name='死球')
    s_out = models.IntegerField(verbose_name='三振')
    double_out = models.IntegerField(verbose_name='併殺打')

    def __str__(self):
        return self.name