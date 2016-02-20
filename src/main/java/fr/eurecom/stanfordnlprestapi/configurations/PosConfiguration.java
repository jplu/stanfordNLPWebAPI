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
package fr.eurecom.stanfordnlprestapi.configurations;

import org.hibernate.validator.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Olivier Varene
 * @author Julien Plu
 */
public class PosConfiguration {
  static final Logger LOGGER = LoggerFactory.getLogger(PosConfiguration.class);
  @NotEmpty
  private String model;

  public PosConfiguration() {
    this.model = "edu/stanford/nlp/models/pos-tagger/english-left3words/"
        + "english-left3words-distsim.tagger";
  }

  public final String getModel() {
    return this.model;
  }

  public final void setModel(final String newModel) {
    this.model = newModel;
  }

  @Override
  public final String toString() {
    return "PosConfiguration{"
        + "model='" + this.model + '\''
        + '}';
  }
}