package com.m2i.search.pattern

import scala.io.Source
import scala.util.matching.Regex


/**
  * Search for patterns in files
  * Use:
  *   search-pattern -i --pattern error --files .../file1.txt,.../file2.txt
  *   search-pattern -i -r --pattern '^[\d]{2,}.*$' --files .../file1.txt,.../file2.txt
  */
class SearchPatternProcessor {

  def searchPattern(files: List[String], pattern: String, regex: Boolean)(implicit ignoreCase: Boolean): List[LineMatch] = {


    files
      .flatMap { file =>
        Source.fromFile(file).getLines.zipWithIndex.toList.flatMap { case (line, index) =>
          regex match {
            case false => boolToOption(withCase(line).contains(withCase(pattern)), LineMatch(file, line, index + 1))
            case true =>
              val regexPattern = pattern.replaceAll("'", "").r
              regexPattern.findFirstIn(line).map(_ => LineMatch(file, line, index + 1))
          }
        }
      }
  }

  private def withCase(line: String)(implicit ignoreCase: Boolean): String = if (ignoreCase) line.toLowerCase else line

  private def boolToOption(expression: Boolean, value: LineMatch): Option[LineMatch] = {
    if (expression) Some(value) else None
  }

}

case class LineMatch(file: String, line: String, nbr: Int)
{
  override def toString: String = {
    s"le fichier $file a la pattern recherché à la ligne $nbr : $line"
  }
}
