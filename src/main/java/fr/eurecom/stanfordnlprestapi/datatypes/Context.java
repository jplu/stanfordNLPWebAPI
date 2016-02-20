/**
 * This file is part of StanfordNLPRESTAPI.
 *
 * StanfordNLPRESTAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * StanfordNLPRESTAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with StanfordNLPRESTAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.eurecom.stanfordnlprestapi.datatypes;

import org.apache.jena.datatypes.xsd.XSDDatatype;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import org.apache.jena.vocabulary.RDF;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.eurecom.stanfordnlprestapi.enums.NlpProcess;

import fr.eurecom.stanfordnlprestapi.interfaces.Sentence;

/**
 * This class represents a NIF context that is aligned to the corresponding Stanford NLP
 * annotations.
 *
 * @author Julien Plu
 */
public class Context {
  static final Logger LOGGER = LoggerFactory.getLogger(Context.class);
  private final String text;
  private final int start;
  private final int end;
  private final List<Sentence> sentences;

  /**
   * Context constructor.
   *
   * @param newText  Text that represents the context.
   * @param newStart Start offset of the context.
   * @param newEnd   End offset of the context.
   */
  public Context(final String newText, final int newStart, final int newEnd) {
    this.sentences = new ArrayList<>();
    this.text = newText;
    this.start = newStart;
    this.end = newEnd;
  }

  public final int start() {
    return this.start;
  }

  public final int end() {
    return this.end;
  }

  public final void addSentence(final Sentence newSentence) {
    this.sentences.add(newSentence);
  }

  public final String text() {
    return this.text;
  }

  /**
   * Turn the context into RDF model.
   *
   * @param tool    Tool used to extract the context.
   * @param process Process required as RDF model.
   *
   * @return RDF model in NIF of the context.
   */
  public final Model rdfModel(final String tool, final NlpProcess process) {
    final String nif = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
    final String base = "http://127.0.0.1/" + tool + '#';
    final Model model = ModelFactory.createDefaultModel();
    final Map<String, String> prefixes = new HashMap<>();

    prefixes.put("nif", nif);
    prefixes.put("local", base);
    prefixes.put("xsd", "http://www.w3.org/2001/XMLSchema#");

    model.setNsPrefixes(prefixes);

    model.add(ResourceFactory.createResource(base + "char=" + this.start + ',' + this.end),
        RDF.type, ResourceFactory.createResource(nif + "String"));
    model.add(ResourceFactory.createResource(base + "char=" + this.start + ',' + this.end),
        RDF.type, ResourceFactory.createResource(nif + "RFC5147String"));
    model.add(ResourceFactory.createResource(base + "char=" + this.start + ',' + this.end),
        RDF.type, ResourceFactory.createResource(nif + "Context"));
    model.add(ResourceFactory.createResource(base + "char=" + this.start + ',' + this.end),
        ResourceFactory.createProperty(nif + "beginIndex"),
        ResourceFactory.createTypedLiteral(Integer.toString(this.start),
            XSDDatatype.XSDnonNegativeInteger));
    model.add(ResourceFactory.createResource(base + "char=" + this.start + ',' + this.end),
        ResourceFactory.createProperty(nif + "endIndex"),
        ResourceFactory.createTypedLiteral(Integer.toString(this.end),
            XSDDatatype.XSDnonNegativeInteger));
    model.add(ResourceFactory.createResource(base + "char=" + this.start + ',' + this.end),
        ResourceFactory.createProperty(nif + "isString"),
        ResourceFactory.createTypedLiteral(this.text));

    for (final Sentence sentence : this.sentences) {
      model.add(sentence.rdfModel(tool, process));
    }

    return model;
  }

  /**
   * Turn the context into RDF string.
   *
   * @param tool    Tool used to extract the context.
   * @param format  Required RDF format.
   * @param process Process required as RDF model (pos or ner).
   *
   * @return RDF string in NIF following the given format.
   */
  public final String rdfString(final String tool, final RDFFormat format,
                                final NlpProcess process) {
    final StringWriter rdf = new StringWriter();

    RDFDataMgr.write(rdf, this.rdfModel(tool, process), format);

    return rdf.toString();
  }

  @Override
  public final String toString() {
    return "Context{"
        + "text='" + this.text + '\''
        + ", start=" + this.start
        + ", end=" + this.end
        + ", sentences=" + this.sentences
        + '}';
  }
}