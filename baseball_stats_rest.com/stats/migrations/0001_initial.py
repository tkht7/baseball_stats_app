# Generated by Django 2.2.5 on 2020-02-07 13:29

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Batting',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=255, verbose_name='名前')),
                ('games', models.IntegerField(verbose_name='試合数')),
                ('team', models.CharField(max_length=255, null=True, verbose_name='チーム')),
                ('handed', models.CharField(choices=[('', '右'), ('*', '左'), ('+', '両')], max_length=2, null=True, verbose_name='席')),
                ('plate_appearances', models.IntegerField(verbose_name='打席')),
                ('runs', models.IntegerField(verbose_name='得点')),
                ('hits', models.IntegerField(verbose_name='安打')),
                ('hits2', models.IntegerField(verbose_name='二塁打')),
                ('hits3', models.IntegerField(verbose_name='三塁打')),
                ('homeruns', models.IntegerField(verbose_name='本塁打')),
                ('runs_batted_in', models.IntegerField(verbose_name='打点')),
                ('steals', models.IntegerField(verbose_name='盗塁')),
                ('caught_steals', models.IntegerField(verbose_name='盗塁死')),
                ('sacrifice_hits', models.IntegerField(verbose_name='犠打')),
                ('sacrifice_flies', models.IntegerField(verbose_name='犠飛')),
                ('bases_on_balls', models.IntegerField(verbose_name='四球')),
                ('intentional_walks', models.IntegerField(verbose_name='故意四')),
                ('hits_by_pitch', models.IntegerField(verbose_name='死球')),
                ('strike_outs', models.IntegerField(verbose_name='三振')),
                ('double_plays', models.IntegerField(verbose_name='併殺打')),
            ],
        ),
        migrations.CreateModel(
            name='Pitching',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('team', models.CharField(max_length=255, null=True, verbose_name='チーム')),
                ('handed', models.CharField(choices=[('', '右'), ('*', '左')], max_length=2, null=True, verbose_name='投')),
                ('name', models.CharField(max_length=255, verbose_name='名前')),
                ('games', models.IntegerField(verbose_name='登板')),
                ('wins', models.IntegerField(verbose_name='勝利')),
                ('loses', models.IntegerField(verbose_name='敗北')),
                ('saves', models.IntegerField(verbose_name='セーブ')),
                ('holds', models.IntegerField(verbose_name='ホールド')),
                ('completes', models.IntegerField(verbose_name='完投')),
                ('shut_out_wins', models.IntegerField(verbose_name='完封')),
                ('no_BB_completes', models.IntegerField(verbose_name='無四球')),
                ('batters_faced', models.IntegerField(verbose_name='対戦打者')),
                ('outs', models.IntegerField(verbose_name='獲得アウト')),
                ('hits', models.IntegerField(verbose_name='被安打')),
                ('homeruns', models.IntegerField(verbose_name='被本塁打')),
                ('bases_on_balls', models.IntegerField(verbose_name='与四球')),
                ('intentional_walks', models.IntegerField(verbose_name='与故意四')),
                ('hits_by_pitch', models.IntegerField(verbose_name='与死球')),
                ('strike_outs', models.IntegerField(verbose_name='奪三振')),
                ('wild_pitches', models.IntegerField(verbose_name='暴投')),
                ('balks', models.IntegerField(verbose_name='ボーク')),
                ('runs', models.IntegerField(verbose_name='失点')),
                ('earned_runs', models.IntegerField(verbose_name='自責点')),
            ],
        ),
    ]
