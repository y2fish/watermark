import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.*;
import com.spire.doc.*;
import com.spire.doc.documents.WatermarkLayout;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Scanner;

public class WaterMark {

    public static void main(String[] args) {
        int flag;//用于选择功能
        PdfDocument pdf = new PdfDocument();//处理PDF对象
        Document document = new Document();//处理word对象
        System.out.println("请选择功能：\n" +
                "1、PDF添加文字水印\n" +
                "2、PDF添加图片水印\n" +
                "3、Word添加文字水印\n" +
                "4、Word添加图片水印\n" +
                "88、输入88退出");
        System.out.print("请输入序号(1/2/3/4/88)：");
        Scanner scanner = new Scanner(System.in);
        flag = scanner.nextInt();
        while (flag != 88) {
            switch (flag) {
                case 1:
                    //1、PDF添加文字水印
                    System.out.print("请输入文本内容：");
                    String textForPDF = scanner.next();//输入水印文本
                    pdf.loadFromFile("./files/test.pdf");//添加水印的文件
                    int pagesforPdftext=pdf.getPages().getCount();//获取文档总页数
                    for (int i=0;i<pagesforPdftext;i++){
                        AddTextWatermarkForPDF(pdf.getPages().get(i), textForPDF); //添加文字水印
                    }
                    pdf.saveToFile("./files/out/WatermarkPdfFromText.pdf");//保存
                    break;
                case 2:
                    //2、PDF添加图片水印
                    pdf.loadFromFile("./files/test.pdf");
                    int pagesforPdfImg=pdf.getPages().getCount();//获取文档总页数
                    for (int i=0;i<pagesforPdfImg;i++){
                        PdfPageBase page = pdf.getPages().get(i);
                        page.setBackgroundImage("./files/img.png");
                        Rectangle2D rect = new Rectangle2D.Float();
                        rect.setFrame(page.getClientSize().getWidth() / 2 - 100, page.getClientSize().getHeight() / 2 - 100, 200, 200);
                        page.setBackgroundRegion(rect);
                    }
                    pdf.saveToFile("./files/out/WatermarkPdfFromImg.pdf");//保存
                    break;
                case 3:
                    //3、Word添加文字水印
                    document.loadFromFile("./files/test.docx");
                    System.out.print("请输入文本内容：");
                    String textForWord = scanner.next();//输入水印文本
                    int pagesforWordtext = document.getSections().getCount();//获取word总页数
                    for (int i=0;i<pagesforWordtext;i++){
                        insertTextWatermark(document.getSections().get(i), textForWord); //添加文字水印
                    }
                    document.saveToFile("./files/out/WatermarkWordFromText.docx", FileFormat.Docx);
                    break;
                case 4:
                    //4、Word添加图片水印
                    document.loadFromFile("./files/test.docx");
                    PictureWatermark picture = new PictureWatermark();
                    picture.setPicture("./files/img.png");
                    picture.setScaling(50);
                    picture.isWashout(false);
                    document.setWatermark(picture);
                    document.saveToFile("./files/out/WatermarkWordFromImg.docx", FileFormat.Docx);
                    break;
                default:
                    System.out.println("输入有误");
            }
            System.out.print("操作完成!\n请输入序号(1/2/3/4/88)：");
            flag = scanner.nextInt();
        }
        System.out.println("感谢使用，再见！");

        pdf.close();//关闭pdf流
        document.close();//关闭word流

    }

    //PDF添加文字水印
    //page:水印页数，textWatermark：文字水印
    static void AddTextWatermarkForPDF(PdfPageBase page, String watermark) {
        Dimension2D dimension2D = new Dimension();
        dimension2D.setSize(page.getCanvas().getClientSize().getWidth() / 2, page.getCanvas().getClientSize().getHeight() / 3);//设置水印2列3行
        PdfTilingBrush brush = new PdfTilingBrush(dimension2D);
        brush.getGraphics().setTransparency(0.3F);//设置透明度
        brush.getGraphics().save();
        brush.getGraphics().translateTransform((float) brush.getSize().getWidth() / 2, (float) brush.getSize().getHeight() / 2);
        brush.getGraphics().rotateTransform(-45);//方向
        brush.getGraphics().drawString(watermark, new PdfTrueTypeFont(new Font("Microsoft Yahei",Font.PLAIN,30),true), PdfBrushes.getRed(), 0, 0, new PdfStringFormat(PdfTextAlignment.Center));//设置文字，字体
        brush.getGraphics().restore();
        brush.getGraphics().setTransparency(1);//设置透明度
        Rectangle2D loRect = new Rectangle2D.Float();
        loRect.setFrame(new Point2D.Float(0, 0), page.getCanvas().getClientSize());
        page.getCanvas().drawRectangle(brush, loRect);//绘制

    }

    //Word添加文本水印
    //section:页面，text：文字水印
    static void insertTextWatermark(Section section, String text) {
        TextWatermark txtWatermark = new TextWatermark();
        txtWatermark.setText(text);//传入参数：要添加的文字
        txtWatermark.setFontSize(40);//字体大小
        txtWatermark.setColor(Color.red);//字体颜色
        txtWatermark.setLayout(WatermarkLayout.Diagonal);//设置布局
        section.getDocument().setWatermark(txtWatermark);//添加水印
    }

}

