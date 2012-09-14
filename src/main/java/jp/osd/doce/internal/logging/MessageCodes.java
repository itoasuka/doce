package jp.osd.doce.internal.logging;

import java.sql.SQLException;

import org.seasar.doma.jdbc.SqlExecutionSkipCause;

/**
 * リソースバンドルのキーとして使用するメッセージコードを定義した列挙型です。
 *
 * @author asuka
 */
public enum MessageCodes {

    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionBegun(String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2063,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionEnded(String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2064,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionSavepointCreated(String, String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2065,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionSavepointReleased(String, String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2066,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionCommitted(String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2067,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionRolledback(String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2068,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionSavepointRolledback(String, String, String, String)}
     * で使用するメッセージコードです。
     */
    DOMA2069,
    /**
     * {@link Slf4jJdbcLogger#logLocalTransactionRollbackFailure(String, String, String, SQLException)}
     * で使用するメッセージコードです。
     */
    DOMA2070,
    /**
     * {@link Slf4jJdbcLogger#logAutoCommitEnablingFailure(String, String, SQLException)}
     * で使用するメッセージコードです。
     */
    DOMA2071,
    /**
     * {@link Slf4jJdbcLogger#logTransactionIsolationSettingFailuer(String, String, int, SQLException)}
     * で使用するメッセージコードです。
     */
    DOMA2072,
    /**
     * {@link Slf4jJdbcLogger#logConnectionClosingFailure(String, String, SQLException)}
     * で使用するメッセージコードです。
     */
    DOMA2073,
    /**
     * {@link Slf4jJdbcLogger#logStatementClosingFailure(String, String, SQLException)}
     * で使用するメッセージコードです。
     */
    DOMA2074,
    /**
     * {@link Slf4jJdbcLogger#logResultSetClosingFailure(String, String, SQLException)}
     * で使用するメッセージコードです。
     */
    DOMA2075,
    /**
     * {@link Slf4jJdbcLogger#logSql(String, String, org.seasar.doma.jdbc.Sql)}
     * で使用するメッセージコードです。
     */
    DOMA2076,
    /**
     * {@link Slf4jJdbcLogger#logDaoMethodEntering(String, String, Object...)}
     * で使用するメッセージコードです。
     */
    EXT001,
    /**
     * {@link Slf4jJdbcLogger#logDaoMethodExiting(String, String, Object)}
     * で使用するメッセージコードです。
     */
    EXT002,
    /**
     * {@link Slf4jJdbcLogger#logDaoMethodThrowing(String, String, RuntimeException)}
     * で使用するメッセージコードです。
     */
    EXT003,
    /**
     * {@link Slf4jJdbcLogger#logSqlExecutionSkipping(String, String, SqlExecutionSkipCause)}
     * で使用するメッセージコードです。
     */
    EXT004,
    /**
     * {@link Logger#logConstructor(Class...)} で使用するメッセージコードです。
     */
    DG001,
    /** <code>{0} = {1}</code> */
    DG002,
    /** 「[{0}]JNDI でルックアップする際に使用するデータソースのオブジェクト名が指定されていないので JNDI からのデータソースの取得はスキップします。」 */
    DG003,
    /** 「[{0}]com.jolbox.bonecp.BoneCPDataSource クラスをロードできないので BoneCPDataSource の生成をスキップします。」 */
    DG004,
    /** 「[{0}]データソースの型が LocalTransactionalDataSource なのでローカルトランザクションを生成します。」 */
    DG005,
    /** 「[{0}]JNDI でオブジェクト名 "{1}" をルックアップします。」 */
    DG006,
    /** 「[{0}]JNDI でオブジェクト名 "{1}" をルックアップしましたが失敗しました。」 */
    DG007,
    /** 「[{0}]JNDI ルックアップ成功 データソースクラス名 = {1}」 */
    DG008,
    /** 「[{0}]ローカルトランザクションを使用するため、データソースを LocalTransactionalDataSource でラップします。」 */
    DG009,
    /** 「[{0}]{1} によるデータソースを提供します。」 */
    DG010,
    /** 「[{0}]ダイアレクトがバインドされているのでそれを使用します。」 */
    DG011,
    /** 「[{0}]ダイアレクトのクラス名として {1} が設定されているのでインスタンス化して使用します。」 */
    DG012,
    /** 「[{0}]ダイアレクトのインスタンス化に失敗しました。」 */
    DG013,
    /** 「[{0}]ダイアレクトを特定する情報が何も無いので StandardDialect を使用します。」 */
    DG014,
    /** 「[{0}]JDBC 接続 URL {1} から推測したダイアレクト {2} を使用します。 」 */
    DG015,
    /** 「[{0}]トランザクションを開始しました。」 */
    DG016,
    /** 「[{0}]トランザクションの開始に失敗しました。」 */
    DG017,
    /** 「[{0}]トランザクションをコミットしました。」 */
    DG018,
    /** 「[{0}]トランザクションのコミットに失敗しました。」 */
    DG019,
    /** 「[{0}]トランザクションをロールバックしました。」 */
    DG020,
    /** 「[{0}]トランザクションのロールバックに失敗しました。」 */
    DG021,
    /** 「[{0}]トランザクションのステータスの取得に失敗しました。」 */
    DG022,
    /** 「[{0}]JDBC ドライバ {1} のロードに失敗しました。」 */
    DG023,
    /** 「不正な値 {0} が指定されたのでデフォルト値 AUTO を使用します。」 */
    DG024,;
}
