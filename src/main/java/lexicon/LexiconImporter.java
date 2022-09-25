package lexicon;

import eu.monnetproject.lemon.LemonModel;
import eu.monnetproject.lemon.LemonSerializer;
import eu.monnetproject.lemon.model.LexicalEntry;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class LexiconImporter {
  private static final String PATH_TO_BASE_FILE = "/base/base.ttl";
  private static Logger LOG = LogManager.getLogger(LexiconImporter.class);

  public LexiconImporter() {}
  
    public LemonModel loadModelFromDir(String dir, String internalResourceDir) throws IOException {
        //System.out.println("dir::"+dir+"  internalResourceDir:"+internalResourceDir);
        final LemonSerializer serializer = LemonSerializer.newInstance();
        LemonModel model = null;
        try ( Stream<Path> paths = Files.walk(Paths.get(dir))) {
            List<Path> list = filterFiles(paths);
            for (Path file : list) {
                 System.out.println("file!!" + file.toString());

                try {
                    if (model == null) {
                        model = serializer.read(new FileReader(file.toString()));
                    } else {
                        LemonModel lm = serializer.read(new FileReader(file.toString()));
                        mergeModels(model, lm);
                    }
                } catch (FileNotFoundException e) {
                    LOG.error("FileNotFoundException: Could not read file {}", file);
                }
            }
            assert model != null;

            LemonModel baseModel = loadBaseFileFromResources(internalResourceDir, serializer);
            mergeModels(model, baseModel);
            return model;
        }
    }

  private LemonModel loadBaseFileFromResources(
    String internalResourceDir,
    LemonSerializer serializer
  ) {
    InputStream inputStream = ClassLoader.getSystemResourceAsStream(internalResourceDir + PATH_TO_BASE_FILE);
    assert inputStream != null;
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    return serializer.read(new BufferedReader(inputStreamReader));
  }

  protected List<Path> filterFiles(Stream<Path> paths) {
    return paths
      .filter(Files::isRegularFile)
      .filter(f -> f.getFileName().toString().endsWith("ttl"))
      .collect(Collectors.toList());
  }

  private void mergeModels(LemonModel model, LemonModel lm) {
    for (eu.monnetproject.lemon.model.Lexicon lexicon : lm.getLexica()) {
      for (LexicalEntry lexicalEntry : lexicon.getEntrys()) {
        model.getLexica().iterator().next().addEntry(lexicalEntry);
      }
    }
  }
}
