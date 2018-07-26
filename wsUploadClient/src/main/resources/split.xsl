<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:redirect="http://xml.apache.org/xalan/redirect"
                xmlns:chx="http://www.idaschox.com/services/CHOX/"
                extension-element-prefixes="redirect"
                version="1.0">
 <!--xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  version="2.0"-->

<xsl:output method="text"/>
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
<xsl:for-each select="//chx:rental">
<xsl:variable name="filename"
  select="concat('splitInput-',position(),'.xml')" />
<!--xsl:value-of select="$filename" /-->  <!-- Creating  -->
    <redirect:write file="{$filename}">
        <chox>
            <xsl:copy-of select="." />
        </chox>
    </redirect:write>
<!--xsl:result-document href="{$filename}" method="xml">
    <chox>
        <xsl:value-of select="."/>
    </chox>
</xsl:result-document-->
</xsl:for-each>
</xsl:template>
</xsl:stylesheet>



<!--xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
        <xsl:output method="xml" version="1.0" encoding="ISO-8859-1" indent="yes"/>
  <xsl:template match="/">
    <xsl:for-each select="ExportInvoice/CustInvoiceJour">
      <xsl:result-document href="C:\\VDP\\TEST\\BE\\Invoice\\Designdata\\{position()}.xml">
      <ExportInvoice>
        <xsl:copy>
          <xsl:apply-templates select="@*"/>
          <xsl:apply-templates select="node()"/>
        </xsl:copy>
        </ExportInvoice>
      </xsl:result-document>
    </xsl:for-each>
  </xsl:template>
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet-->