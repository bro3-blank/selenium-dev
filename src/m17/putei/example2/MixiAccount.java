package m17.putei.example2;

/**
 * Mixiのアカウント情報を保持するクラス
 */
public class MixiAccount {

  //こういうふうにプライベート変数にして他のクラスからの変更を防ぐことを
  //オブジェクト指向プログラミングでは「カプセル化」といいます。
  private String mixiEmail;
  private String mixiPassword;
  
  /**
   * 引数が与えられた時はその値で初期化
   * @param mixiEmail
   * @param mixiPassword
   */
  public MixiAccount( String mixiEmail, String mixiPassword ) {
    //ローカル変数をクラス変数に代入
    this.mixiEmail = mixiEmail;
    this.mixiPassword = mixiPassword;
  }
  
  /**
   * 引数が与えられなかった時はシステムプロパティで初期化
   * メールアドレスとパスワードは-Dオプションで指定。設定例：
   * http://gyazo.com/e3ee52fb8bdf2206478da397fccbe174
   */
  public MixiAccount() {
    //この場合、this.mixiEmail と mixiEmailは同じクラス変数をさします
    mixiEmail = System.getProperty("mixi.email");
    mixiPassword = System.getProperty("mixi.password");
    
    //パスワードちゃんと入ってるかチェック
    if (mixiEmail==null || mixiEmail.length()==0 || 
            mixiPassword==null || mixiPassword.length()==0 ) {
      System.err.println("メールアドレスとパスワードをDオプションで指定してください。\n例： http://gyazo.com/e3ee52fb8bdf2206478da397fccbe174");
      System.exit(-1);
    }
  }

  //Getterメソッド
  public String getMixiEmail() {
    return mixiEmail;
  }

  //Getterメソッド
  public String getMixiPassword() {
    return mixiPassword;
  }
  
}
