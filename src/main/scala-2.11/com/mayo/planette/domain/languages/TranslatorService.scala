package com.mayo.planette.domain.languages

import com.mayo.whatis.lang.Sentence
import com.mayo.whatis.mean.Meaning
import scala.concurrent.Future

/**
 * @author yoav @since 5/6/16.
 */
trait TranslatorService {
  type RequestMeaning <: Meaning
  type RequestSentence <: Sentence
  type RequestMeaningExists = Boolean

  def translatePhraseToRequest(requestSentence: RequestSentence): Future[RequestMeaning]
  def requestMeaningExists(requestSentence: RequestSentence): Future[RequestMeaningExists]
}
