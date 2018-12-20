package pico.erp.shared;

public final class TypeDefinitions {

  public static final int ADDRESS_DETAIL_LENGTH = 50;

  public static final int ADDRESS_POSTAL_LENGTH = 10;

  public static final int ADDRESS_STREET_LENGTH = 50;

  public static final int CLASS_NAME_LENGTH = 200;

  public static final int CLOB_LENGTH = 1024 * 1024;

  public static final int CODE_LENGTH = 20;

  public static final int COMMENT_LENGTH = 200;

  public static final int CONTENT_TYPE_LENGTH = 100;

  public static final int DESCRIPTION_LENGTH = 200;

  public static final int EMAIL_LENGTH = 30;

  public static final int ENUM_LENGTH = 20;

  public static final int EXTERNAL_ID_LENGTH = 100;

  public static final int FILE_NAME_LENGTH = 300;

  public static final int HIERARCHY_REFERENCE_LENGTH = 500;

  public static final int HUMAN_NAME_LENGTH = 20;

  public static final int ID_LENGTH = 50;

  public static final int NAME_LENGTH = 50;

  public static final int NAME_X2_LENGTH = 100;

  public static final int NAME_X3_LENGTH = 150;

  /**
   * 암호 암호화 컬럼 길이 단반향 암호화로 5배수 길이 지정
   */
  public static final int PASSWORD_ENCODED_LENGTH = 100;

  public static final int PASSWORD_LENGTH = 20;

  /**
   * 파일 경로 허용 정규식
   */
  public static final String PATH_NAME_REGEXP = "[^<^>^:^\"^/^\\^|^?^*]+";

  public static final int PHONE_NUMBER_LENGTH = 20;

  public static final int REMARK_LENGTH = 50;

  /**
   * 직위/직급
   */
  public static final int TITLE_LENGTH = 20;

  public static final int UUID_BINARY_LENGTH = 16;

  private TypeDefinitions() {
  }

}
