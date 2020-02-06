from stats.models import Result
import requests
from bs4 import BeautifulSoup
import time


# スクレイピング
team_list = ['l','h','f','b','m','e','c','s','g','db','d','t']
for team_token in team_list:
    URL = 'http://npb.jp/bis/2019/stats/idb1_{}.html'.format(team_token)
    res = requests.get(URL)
    res.encoding = res.apparent_encoding
    soup = BeautifulSoup(res.content, 'html.parser') # BeautifulSoupの初期化
    tags = soup.find_all('tr', class_='ststats')


    result_obj = []
    for tag in tags:
        result = []
        data = tag.find_all('td')
        for i in range(len(data)): # 打数，塁打，打率，長打率，出塁率を除く
            if not(i == 4 or i == 10 or i == 21 or i == 22 or i == 23):
                result.append(data[i].text.replace('\u3000', ' '))
        # print(result)
        b = Result(team = team_token,
                    dominant = result[0],
                    name = result[1],
                    b_num = result[2],
                    point = result[3],
                    hit = result[4],
                    hit2 = result[5],
                    hit3 = result[6],
                    homerun = result[7],
                    b_point = result[8],
                    steal = result[9],
                    steal_out = result[10],
                    s_hit = result[11],
                    s_fly = result[12],
                    fourball = result[13],
                    intent_four = result[14],
                    deadball = result[15],
                    s_out = result[16],
                    double_out = result[17],
        )
        result_obj.append(b)

    Result.objects.bulk_create(result_obj)

    time.sleep(1)
