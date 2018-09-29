package com.m2i.search.pattern

import org.slf4j.LoggerFactory

/**
  * Used to search pattern in files:
  *   search-pattern -i --pattern error --files .../logs/file1.txt,.../logs/file2.txt
  *   search-pattern -i -r  --pattern '^[\d]{2}.*$' --files .../logs/file1.txt,.../logs/file2.txt
 */
object SearchPatternBoot extends App {

  private val logger = LoggerFactory.getLogger(this.getClass.getName)

  val bootConfig: SearchPatternConfig = parseArgument()

  implicit val ignoreCase = bootConfig.ignoreCase

  val processor = new SearchPatternProcessor()
  val searchResult: List[LineMatch] = processor.searchPattern(
    bootConfig.files,
    bootConfig.pattern,
    bootConfig.regex)

  searchResult.foreach(println)

  logger.info("End searching patterns")

  def parseArgument(): SearchPatternConfig =
  {

    val parser = new scopt.OptionParser[SearchPatternConfig]("search-pattern") {
      head("search pattern in files")
      opt[Seq[String]]('f', "files") required() maxOccurs (1) action { (x, c) ⇒
        c.copy(files = x.toList)
      } text ("files to search")
      opt[String]('p', "pattern") required () maxOccurs (1) action { (x, c) ⇒
        c.copy(pattern = x)
      } text ("pattern to search")
      opt[Unit]('i', "ignoreCase") optional () maxOccurs (1) action { (_, c) ⇒
        c.copy(ignoreCase = true)
      } text ("should ignore case when search")
      opt[Unit]('r', "regex") optional () maxOccurs (1) action { (_, c) ⇒
        c.copy(regex = true)
      } text ("should search for regex")
      help("help") text ("prints this usage text")
    }

    parser.parse(args, SearchPatternConfig(files = Nil, pattern = "")) match {
      case Some(config) ⇒ config
      case None ⇒
        parser.showUsage
        throw new IllegalArgumentException("Error in boot config")
    }
  }

}

case class SearchPatternConfig(files: List[String],
                               pattern: String,
                               ignoreCase: Boolean = false,
                               regex: Boolean = false)
