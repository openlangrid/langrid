言語グリッド

日本語, [English](/README.md)
---------------------------------

言語グリッドは，世界中の言語サービスの蓄積と共有を行うWebサービスプラットフォームです．
現在，以下の機能が提供されています．
* 言語グリッドで共有されている言語サービスを呼び出すためのAPIライブラリ
* 既存の言語関連ソフトウェアから言語グリッド対応のWebサービス（言語サービス）を開発するためのラッパーライブラリ
* 既存の言語サービスを組み合わせて，新たな言語サービスを作成するライブラリ

# 言語グリッドのユーザになる
言語グリッドのユーザになると，言語グリッドを介して言語サービスの利用や公開ができます．  
言語グリッドのユーザになるには，以下のリンク先の手順にしたがって登録を行ってください．
言語グリッドの利用手続き：http://langrid.org/operation/jp/procedure.html

# 言語サービスを呼び出す
言語グリッドのユーザは，言語グリッドに登録されているサービスを用いて，独自の言語アプリケーションを開発することができます．
以下のリンクで，言語グリッドに登録されている言語サービスを呼び出すためのAPI(Java, PHP)を公開しています．
* 多言語工房：http://langrid.org/developer/jp/

このAPIを用いてサービスを呼び出す際は，ユーザ登録後に発行される言語グリッドのユーザIDとパスワードが必要です．
また，呼び出したいサービスのWSDL情報も必要です（多言語工房のチュートリアル（http://langrid.org/developer/jp/phplibrary_tutorial.html）を参考）
呼び出し可能なサービスについては下記リンクの言語グリッドサービスマネージャで探すことができます．
* 言語グリッドサービスマネージャ：http://langrid.org/service_manager/

# 言語サービスを開発する
言語グリッドのユーザは，自身が開発した手持ちの翻訳ソフトウェアや，多言語辞書などの言語資源を用いて言語サービスを開発することができます．
手持ちの言語資源を用いて言語サービスを開発するには，下記リンクの言語サービス作成マニュアルを参考にしてください．
* 言語サービス作成マニュアル：https://sourceforge.net/projects/servicegrid/files/Language%20Grid/language-service-creation-20110711-ja.pdf/download

作成した言語サービスを，Webサーバ上に配備したのち，言語グリッドサービスマネージャに登録することが可能です．
下記リンクの，サービスマネージャのマニュアルを参考にして登録してください．
* サービスマネージャのマニュアル：http://langrid.org/~otani/bootstrap3/nantes/ServiceManager_Manual_en.pdf

# 言語グリッドの開発に参加する
言語グリッドの基盤ソフトウェアはLGPLで公開されており，ユーザ・非ユーザにかかわらず，言語グリッドの拡張などの開発に参加できます．
詳しくは，言語グリッドOSSプロジェクトを参照ください．
* 言語グリッドOSSプロジェクト：http://langrid.org/oss-project/jp/index.html

# ライセンス
GNU Library or Lesser General Public License version 2.0 (LGPLv2)

# 注意事項
言語グリッドは，Webサービスの蓄積と共有を行うためのサーバソフトウェアService Gridを基盤ソフトウェアとしています．  
本プロジェクトに関連するソフトウェアを用いて開発したソフトウェアを公開する際には，下記の記述をソフトウェアのWebサイトやドキュメントに含めるようお願いいたします．  

* This software uses the [SOFTWARE] by the Language Grid project (http://langrid.org/ ).

[SOFTWARE] is one of:
* Service Grid Server Software (http://langrid.org/oss-project/en/service_grid.html )
* Language Service Development Libraries (http://langrid.org/oss-project/en/language_service.html )
* Language Grid Toolbox (http://langrid.org/oss-project/en/toolbox.html )

また，本プロジェクトに関連するソフトウェアを用いて論文や本などの書籍を出版される際は，以下の書籍の引用をお願いいたします．

* Toru Ishida Ed. The Language Grid: Service-Oriented Collective Intelligence for Laguage Resource Interoperability. Springer, 2011. ISBN 978-3-642-21177-5.

# リンク
本リポジトリは，NPO法人言語グリッドアソシエーションによって運営されています．
* http://langrid.org/
