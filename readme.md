###コンパイル方法
```
 javac JabberServer.java
 javac JabberClient.java
 javac BlackJack.java
```
###実行方法
- トランプの画像ファイルが別途必要
- 片方のPCで`java JabberServer`
- 二つのPCで`java BlackJack`

カードゲーム共通の機能(カードの描画等)をCardGameクラス、
BlackJack特有のプレイ方法やルールをBlackJackクラスに分けた(つもり)です。
