package util.io;

import java.io.FileNotFoundException;
import java.net.URL;

import static java.util.Objects.isNull;

public class ResourceHelper {

  public static URL loadResource(String resource, Class<?> clazz) throws FileNotFoundException {
    URL res = clazz.getClassLoader().getResource(resource);
    if (isNull(res)) {
      throw new FileNotFoundException(String.format("FileNotFound: %s", resource));
    }
    return res;
  }
}
