import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ValidateSchema {

    public static void main(String[] args) {
        try {
            Processor processor = new Processor(false);
            XsltCompiler compiler = processor.newXsltCompiler();
            XsltExecutable xslt = compiler.compile(new StreamSource(
                new File("target/result.xsl")
            ));
            XsltTransformer transformer = xslt.load();

            transformer.setSource(new StreamSource(new File("example.xml")));
            XdmDestination chainResult = new XdmDestination();
            transformer.setDestination(chainResult);
            transformer.transform();

            List<String> errorList = new ArrayList<>();
            XdmNode rootnode = chainResult.getXdmNode();
            for (XdmNode node : rootnode.children().iterator().next().children()) {
                if(!"failed-assert".equals(node.getNodeName().getLocalName())) continue;
                String res = node.children().iterator().next().getStringValue();
                errorList.add(res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
