package pico.erp.shared.data;

import java.io.InputStream;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

@Builder
public class ContentInputStream extends InputStream {

  public static final String XLSX_CONTENT_EXTENSION = "xlsx";

  public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  @Delegate
  private final InputStream inputStream;

  @Getter
  private final String name;

  @Getter
  private final String contentType;

  @Getter
  private final long contentLength;


}
