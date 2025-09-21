package org.example;

public class Template {

    public static String templateHeaderReq(String serviceName, String docNameReq){
        // PREPARE XML TEMPLATE HEADER
        String xmlTemplateHeaderReq = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
                "<Values version=\"2.0\">\n" +
                "  <record name=\"record\" javaclass=\"com.wm.util.Values\">\n" +
                "    <value name=\"node_type\">record</value>\n" +
                "    <value name=\"node_subtype\">unknown</value>\n" +
                "    <value name=\"node_nsName\">"+serviceName+".documents:"+docNameReq+"</value>\n" +
                "    <value name=\"node_pkg\">"+serviceName+"</value>\n" +
                "    <value name=\"node_comment\"></value>\n" +
                "    <value name=\"is_public\">false</value>\n" +
                "    <value name=\"field_type\">record</value>\n" +
                "    <value name=\"field_dim\">0</value>\n" +
                "    <value name=\"wrapper_type\">IData</value>\n" +
                "    <value name=\"nillable\">true</value>\n" +
                "    <value name=\"form_qualified\">false</value>\n" +
                "    <value name=\"is_global\">false</value>\n" +
                "    <array name=\"rec_fields\" type=\"record\" depth=\"1\">\n" +
                "      <record javaclass=\"com.wm.util.Values\">\n" +
                "        <value name=\"node_type\">record</value>\n" +
                "        <value name=\"node_subtype\">unknown</value>\n" +
                "        <value name=\"node_comment\"></value>\n" +
                "        <record name=\"node_hints\" javaclass=\"com.wm.util.Values\">\n" +
                "          <value name=\"field_largerEditor\">false</value>\n" +
                "          <value name=\"field_password\">false</value>\n" +
                "        </record>\n" +
                "        <value name=\"is_public\">false</value>\n" +
                "        <value name=\"field_name\">requestBody</value>\n" +
                "        <value name=\"field_type\">record</value>\n" +
                "        <value name=\"field_dim\">0</value>\n" +
                "        <value name=\"wrapper_type\">IData</value>\n" +
                "        <value name=\"nillable\">true</value>\n" +
                "        <value name=\"form_qualified\">false</value>\n" +
                "        <value name=\"is_global\">false</value>\n" +
                "        <array name=\"rec_fields\" type=\"record\" depth=\"1\">";
        return xmlTemplateHeaderReq;
    }

    public static String templateFooterReq(){
        String xmlTemplateFooterReq = "\n        </array>\n" +
                "        <value name=\"rec_closed\">true</value>\n" +
                "        <value name=\"modifiable\">true</value>\n" +
                "      </record>\n" +
                "    </array>\n" +
                "    <value name=\"modifiable\">true</value>\n" +
                "  </record>\n" +
                "</Values>";
        return  xmlTemplateFooterReq;
    }

    public static String dynamicTemplateReq(){
        String xmlDynamicTemplateReq = "\n        <record javaclass=\"com.wm.util.Values\">\n" +
                "            <value name=\"node_type\">record</value>\n" +
                "            <value name=\"node_subtype\">unknown</value>\n" +
                "            <value name=\"node_comment\"></value>\n" +
                "            <record name=\"node_hints\" javaclass=\"com.wm.util.Values\">\n" +
                "              <value name=\"field_usereditable\">true</value>\n" +
                "              <value name=\"field_largerEditor\">false</value>\n" +
                "              <value name=\"field_password\">false</value>\n" +
                "            </record>\n" +
                "            <value name=\"is_public\">false</value>\n" +
                "            <value name=\"field_name\">%s</value>\n" +
                "            <value name=\"field_type\">string</value>\n" +
                "            <value name=\"field_dim\">0</value>\n" +
                "            <array name=\"field_options\" type=\"value\" depth=\"1\">\n" +
                "            </array>\n" +
                "            <value name=\"nillable\">true</value>\n" +
                "            <value name=\"form_qualified\">false</value>\n" +
                "            <value name=\"is_global\">false</value>\n" +
                "          </record>";
        return xmlDynamicTemplateReq;
    }


    public static String templateHeaderRes(String serviceName, String docNameRes){
        //PREPARE TEMPLATE RESPONSE XML
        String xmlTemplateHeaderRes = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n" +
                "<Values version=\"2.0\">\n" +
                "  <record name=\"record\" javaclass=\"com.wm.util.Values\">\n" +
                "    <value name=\"node_type\">record</value>\n" +
                "    <value name=\"node_subtype\">unknown</value>\n" +
                "    <value name=\"node_nsName\">"+serviceName+".documents:"+docNameRes+"</value>\n" +
                "    <value name=\"node_pkg\">"+serviceName+"</value>\n" +
                "    <value name=\"node_comment\"></value>\n" +
                "    <value name=\"is_public\">false</value>\n" +
                "    <value name=\"field_type\">record</value>\n" +
                "    <value name=\"field_dim\">0</value>\n" +
                "    <value name=\"wrapper_type\">IData</value>\n" +
                "    <value name=\"nillable\">true</value>\n" +
                "    <value name=\"form_qualified\">false</value>\n" +
                "    <value name=\"is_global\">false</value>\n" +
                "    <array name=\"rec_fields\" type=\"record\" depth=\"1\">\n" +
                "      <record javaclass=\"com.wm.util.Values\">\n" +
                "        <value name=\"node_type\">record</value>\n" +
                "        <value name=\"node_subtype\">unknown</value>\n" +
                "        <value name=\"node_comment\"></value>\n" +
                "        <record name=\"node_hints\" javaclass=\"com.wm.util.Values\">\n" +
                "          <value name=\"field_largerEditor\">false</value>\n" +
                "          <value name=\"field_password\">false</value>\n" +
                "        </record>\n" +
                "        <value name=\"is_public\">false</value>\n" +
                "        <value name=\"field_name\">responseBody</value>\n" +
                "        <value name=\"field_type\">record</value>\n" +
                "        <value name=\"field_dim\">0</value>\n" +
                "        <value name=\"wrapper_type\">IData</value>\n" +
                "        <value name=\"nillable\">true</value>\n" +
                "        <value name=\"form_qualified\">false</value>\n" +
                "        <value name=\"is_global\">false</value>\n" +
                "        <array name=\"rec_fields\" type=\"record\" depth=\"1\">";
        return xmlTemplateHeaderRes;
    }

    public static String templateFooterRes(){
        String xmlTemplateFooterRes = "\n        </array>\n" +
                "        <value name=\"rec_closed\">true</value>\n" +
                "        <value name=\"modifiable\">true</value>\n" +
                "      </record>\n" +
                "    </array>\n" +
                "    <value name=\"modifiable\">true</value>\n" +
                "  </record>\n" +
                "</Values>";
        return  xmlTemplateFooterRes;
    }

    public static String dynamicTemplateRes(){
        String xmlDynamicTemplateRes = "\n        <record javaclass=\"com.wm.util.Values\">\n" +
                "            <value name=\"node_type\">record</value>\n" +
                "            <value name=\"node_subtype\">unknown</value>\n" +
                "            <value name=\"node_comment\"></value>\n" +
                "            <record name=\"node_hints\" javaclass=\"com.wm.util.Values\">\n" +
                "              <value name=\"field_usereditable\">true</value>\n" +
                "              <value name=\"field_largerEditor\">false</value>\n" +
                "              <value name=\"field_password\">false</value>\n" +
                "            </record>\n" +
                "            <value name=\"is_public\">false</value>\n" +
                "            <value name=\"field_name\">%s</value>\n" +
                "            <value name=\"field_type\">string</value>\n" +
                "            <value name=\"field_dim\">0</value>\n" +
                "            <array name=\"field_options\" type=\"value\" depth=\"1\">\n" +
                "            </array>\n" +
                "            <value name=\"nillable\">true</value>\n" +
                "            <value name=\"form_qualified\">false</value>\n" +
                "            <value name=\"is_global\">false</value>\n" +
                "          </record>";
        return xmlDynamicTemplateRes;
    }
}
