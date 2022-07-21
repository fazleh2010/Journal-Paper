package evaluation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

import java.util.List;
import util.io.LcQuadElement;

public class QALD {
  @JsonProperty
  public QALDDatasetDefinition dataset;
  @JsonProperty
  public List<QALDQuestions> questions=new ArrayList<QALDQuestions> ();

    public QALD(String datasetString, List<LcQuadElement> lcQuadElements) {
        dataset = new QALDDatasetDefinition(datasetString);
        for (LcQuadElement LcQuadElement : lcQuadElements) {
            QALDQuestions qaldQuestion = new QALDQuestions(LcQuadElement);
            questions.add(qaldQuestion);
        }
        
       
    }

  public static class QALDDatasetDefinition {
    @JsonProperty
    String id;
    
    public QALDDatasetDefinition(String id){
        this.id=id;
    }

        @Override
        public String toString() {
            return "QALDDatasetDefinition{" + "id=" + id + '}';
        }
    
  }

  public static class QALDQuestion {
    @JsonProperty
    public String language;
    @JsonProperty
    public String string;
    @JsonProperty
    public String keywords;
    
    public QALDQuestion(String language,String string, String keywords){
    this.language=language;
    this.string=string;
    this.keywords=keywords;
    }

        @Override
        public String toString() {
            return "QALDQuestion{" + "language=" + language + ", string=" + string + ", keywords=" + keywords + '}';
        }
    
  }

  public static class QALDQuery {
    @JsonProperty
    public String sparql;
    
    public QALDQuery(String sparql){
        this.sparql=sparql;
    }

        @Override
        public String toString() {
            return "QALDQuery{" + "sparql=" + sparql + '}';
        }
    
  }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QALDQuestions {

        @JsonProperty
        public String id;
        @JsonProperty
        public String answertype;
        @JsonProperty
        public List<QALDQuestion> question;
        @JsonProperty
        public QALDQuery query;

        public QALDQuestions(LcQuadElement lcQuadElemen) {
            System.out.println(" lcQuadElemen.getId()::" + lcQuadElemen.getId());
            System.out.println(" lcQuadElemen.getSparql_template_id()::" + lcQuadElemen.getSparql_template_id());
            System.out.println(lcQuadElemen.getEnglish() + " getQuestion_en::" + lcQuadElemen.getQuestion_en());
            System.out.println(lcQuadElemen.getQuestion_de() + " getQuestion_de::" + lcQuadElemen.getQuestion_de());
            System.out.println("getSparql_query::" + lcQuadElemen.getSparql_query());
            this.id = lcQuadElemen.getId().toString();
            this.answertype = lcQuadElemen.getSparql_template_id().toString();
            this.question = getQuestion(lcQuadElemen);
            this.query = new QALDQuery(lcQuadElemen.getSparql_query());
        }

        public List<QALDQuestion> getQuestion(LcQuadElement lcQuadElemen) {
            List<QALDQuestion> question = new ArrayList<QALDQuestion>();
            if (lcQuadElemen.getQuestion_en() != null) {
                question.add(new QALDQuestion(lcQuadElemen.getEnglish(), lcQuadElemen.getQuestion_en(), "keywords"));
            }
            if (lcQuadElemen.getQuestion_de() != null) {
                question.add(new QALDQuestion(lcQuadElemen.getQuestion_de(), lcQuadElemen.getQuestion_de(), "keywords"));
            }
            return question;
        }

    }

    @Override
    public String toString() {
        return "QALD{" + "dataset=" + dataset + ", questions=" + questions + '}';
    }
  
}
