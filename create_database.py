import requests
from bs4 import BeautifulSoup

from batting_list.models import Result

# クローリング・スクレイピング
res = requests.get("http://npb.jp/bis/2019/stats/idb1_g.html")
res.encoding = res.apparent_encoding
soup = BeautifulSoup(res.content, 'html.parser') # BeautifulSoupの初期化

tags = soup.find_all("tr", class_="ststats")

result_obj = []
for tag in tags:
    result = []
    data = tag.find_all("td")
    for i in range(len(data)-3): # 打数，塁打，打率，長打率，出塁率を除く
        if i != 0 and i != 4 and i != 10:
            result.append(data[i].text.replace("\u3000", ' '))
    # print(result)
    b = Result(name=result[0],
                b_num = result[1],
                point = result[2],
                hit = result[3],
                hit2 = result[4],
                hit3 = result[5],
                homerun = result[6],
                b_point = result[7],
                steal = result[8],
                steal_out = result[9],
                s_hit = result[10],
                s_fly = result[11],
                fourball = result[12],
                intent_four = result[13],
                deadball = result[14],
                s_out = result[15],
                double_out = result[16],
    )
    result_obj.append(b)

Result.object.bulk_create(result_obj)
    