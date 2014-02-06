<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"

	version="1.0">

	<!-- http://nwalsh.com/docs/articles/dbdesign/ -->

	<!-- imports the original docbook stylesheet -->
	<xsl:import href="urn:docbkx:stylesheet" />

	<!-- set bellow all your custom xsl configuration -->

	<xsl:param name="highlight.source" select="1"/>

	<!-- Important links:
	  - http://www.sagehill.net/docbookxsl/
	  - http://docbkx-tools.sourceforge.net/
	  -->
	<xsl:attribute-set name="monospace.verbatim.properties">
		<xsl:attribute name="font-size">
			<xsl:choose>
				<xsl:when test="processing-instruction('db-font-size')">
					<xsl:value-of select="processing-instruction('db-font-size')" />
				</xsl:when>
				<xsl:otherwise>inherit</xsl:otherwise>
			</xsl:choose>
		</xsl:attribute>
	</xsl:attribute-set>

 <xsl:template match="emphasis[@role='strong-italic']">
   <fo:inline xmlns:fo="http://www.w3.org/1999/XSL/Format"
   font-weight="bold" font-style="italic">
     <xsl:apply-templates/>
   </fo:inline>
 </xsl:template>
</xsl:stylesheet>