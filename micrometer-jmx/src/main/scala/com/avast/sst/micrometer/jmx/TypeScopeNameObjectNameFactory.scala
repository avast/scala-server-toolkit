package com.avast.sst.micrometer.jmx

import java.util
import java.util.regex.Pattern

import com.codahale.metrics.jmx.{DefaultObjectNameFactory, ObjectNameFactory}
import javax.management.ObjectName

import scala.util.Try;

/** This is custom [[com.codahale.metrics.jmx.ObjectNameFactory]] which uses "type-scope-name" hierarchy of resulting
  * [[javax.management.ObjectName]] (levels 3-N are glued together).
  */
class TypeScopeNameObjectNameFactory(separator: String = ".") extends ObjectNameFactory {

  private val quotedSeparator = Pattern.quote(separator)

  private val defaultFactory = new DefaultObjectNameFactory()

  private val partNames = Vector("type", "scope", "name")

  override def createName(`type`: String, domain: String, name: String): ObjectName = {
    val parsedName = parseName(domain, name)
    parsedName.getOrElse(defaultFactory.createName(`type`, domain, name))
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  private def parseName(domain: String, name: String): Try[ObjectName] = Try {
    val parts = name.split(quotedSeparator, partNames.length)

    /* The following block of code is a little hack. The problem is that ObjectName requires HashTable as parameter but HashTable
       is unsorted and thus unusable for us. We hack it by raping the HashTable and in-fact using LinkedHashMap which is
       much more suitable for our needs. */
    val map = new java.util.LinkedHashMap[String, String](parts.length)
    val properties = new java.util.Hashtable[String, String](parts.length) {
      override def entrySet(): util.Set[util.Map.Entry[String, String]] = map.entrySet()
    }

    parts.zip(partNames).foreach {
      case (part, partName) =>
        val quoted = quote(part)
        properties.put(partName, quoted)
        map.put(partName, quoted)
    }

    new ObjectName(domain, properties)
  }

  private def quote(objectName: String) = objectName.replaceAll("[\\Q.?*\"\\E]", "_")

}
