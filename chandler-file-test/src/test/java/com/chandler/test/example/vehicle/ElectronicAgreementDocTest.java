/*
 * chandler-file-test
 * 2025/3/21 15:28
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.vehicle;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.net.URIBuilder;
import org.assertj.core.util.Lists;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/3/21 15:28
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class ElectronicAgreementDocTest {
    public static void main(String[] args) {
        List<String> fileIds= Lists.newArrayList("efe0166a4ce5407da0a4cafece5894fb","8318f266d8594837a7cee950dc3f32fd","24a3b1f00ce14c34b8af4e7f79f6124f","e656a835f76648fc958fcf82ea8878e5","c052b44202b3407ea7931bf530f526bf","6eb4e344d18d43bd9478f613bf379c7c","8024091e7a4f4db7bd53eb70bbc2c931","5a48febb10c846cbab7a87916bdaf063","1205ce9a87864cbab081d4f35c824715","3d986f0c6eb94f30ab1d88cdfa9a35e1","10648bb3f674438a8033ac53e02f9371","3ba7e5e477d947b18d0a1cf4bdd47f93","cdfe0832f00f48b0bc432bef0861e195","75d7d479578c45659e1711fd9be10c22","58315b5d5d9346e797a8470cb71b5a5e","73a5fb2df4e8499dac56a9f77c693fd9","cc0775c73da04e278cb6fa16dfb783f9","410fe0ecbb4d4c58bf44a82d755fa1d5","e214af9a377142a5aec121fdb84a1cc9","8b344ae7995e466087b461cc564c32eb","49a9d117477b4f73a9dab80545786444","ac76a75953734087bb3a64e62e0d7604","aeb93a21706e470cb00e5307164d7525","c419b7dd9a214db7bb2841276fddd6aa","d0b73ac40009437d8355c2b909980f3a","40d18358265949189059a8548b66d9cd","ef016aeee6f74029aa5e8ad6683980d8","c85f8df6bc5a4d769a42e20f03561a29","7c0a7d4632b0495a939a2a15bf0e9720","145692cc68554e5a95e0699d61c9af17","b8f5bf0c6be94de5a8634a9086d012b5","ea3fa08e684a4cebac7af531bb7994e7","ab7cfce9ac324aea9b2c382749041dce","afc9161425b942e7bc2e59faf53a00e8","691477181e0a4c7dacfdcfecc0b5c65b","78c2283eb8134dde96cca2254741e872","4a8cd74778964ca49b07a2e4f4703c74","5fad9087701742feb391c93703d830d6","5edf63285d4143af9b9eeddee001ad32","ab0048d66a9a46e3a00a0056d4b2d76c","ba35b3619b054f789df544b2221e77b0","404b55cb77d84de49951a851b31bb88a","40fb020c413c48c8805a5d21685cc790","d43c8a2b86284129875dd31188f57325","41db4f252e6d47cc83924501a33164ec","6693e7f9bbdc4a4e8b7c84219aa36691","26a1945272d643e7b79b5f1deaad51c5","2038666aae264092bd6b61033802dd6c","cf1538b40556457d88b60abb510c7e83","7644e03afc2a43038c6e3366ea7b9d41","3af299ba195140d9b3731df60a851d18","2214510c563c4accbb244038c0750731","b78479e9e71148e4a7b16dc18a12a04b","8902a18e103f4b6d9812c927673f4e1d","76e93a6340044f438bc9b6ade85152fa","2e6ca0755a694ba9a7bae5bb844961a3","eaad543adc544821955a556679b3a669","42fdee9dd5b84e31971fb867e758ffa1","8798894424fc44de97006cfbd99f51ae","3c22df6b8bb1446e9456b9523c37ca2f","ea3be63efa8e40a2a11f0ab7a2b86698","2968fee37746441d8b2448c523d1ae0a","b1ce66277b634ba398a1bfbbef193a7d","7df983bd242e40a9857a386042020a8d","331fe3c2ae094620afb4d7c18b3cc483","44f5256e428a4a01a887f373ca0b7464","d2c36273eb57451c9e01e880ebba9751","aa33bfad6fc84cc98a4e2dec25acb624","4f55101268814ac592cf8687252ff66f","8c6e87a3da0549d5a04f8cf84f31fafc","4c7d24934af84718ba18e3fc7a79e431","5007d6efe3c9426e9f2a7fe2ff09bb20","f24ef6d131974fc8b2cc8360b836a244","9cf08df9d2b04a559f0fba2bd34c0ded","4f0b3c15c563405bb0d9c1da336d580a","0fce52d358d64d6da14eb23440335b98","147cf9231e1144369ad0e2b8d61950cc","061a447e75584e5281919a212a6969a6","752383e054454a61883f3e16418dda0a");
        fileIds.forEach(fileId->{
            Request request = null;
            try {
                URI uri =
                        new URIBuilder(new URI("https://api.carrierglobe.com/vehicle-management/agreement/signed/file/view?fileId="+fileId))
                                .build();
                request =
                        Request.get(uri)
                                .setHeader(new BasicHeader("authorization", "eyJ0eXAiOiJKV1QiLCJ2ZXJzaW9uIjoiVjMwIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyMzI4NTIwODE0NTE2MjI0OCIsImp0aSI6IjM2MDM0ODIyNDI4OTIxNjY3MiIsImlzcyI6Ijk1MTg1NjY2NTExMTk2NDMiLCJpYXQiOjE3NDI1MzYyNjMsImV4cCI6MTc0MjYwODI2MywiYXVkIjoiMjg3OTk3MTg1MTk3MzYzNTYiLCJyIjoiXHVEODQ2XHVERTc45ZqRy7tcdUQ4NjhcdURDRkMiLCJuaWNrIjoi6ZKx5LiB5ZCbIiwiaW5kZW50aXR5IjoyMzI4NTIwODE0NTE2MjI0OCwib3JnUm9vdElkIjoxMDI5OTcyMjQxODA2OTUwNCwiaHVpZCI6InFkajEzOTkiLCJjbGllbnQiOiIifQ.Pmhp9asmLIksPLbc7zZG3MXjnDS-UtkI971RSE-9AnckQ9_-YgUwlMwAGQlCDH88ufpvFI4cdJlO6rL2WtY_mw"))
                ;
                Response response = request.execute();

                FileOutputStream outstream = new FileOutputStream(String.format("/Users/chandler/Downloads/电子合同/%s.pdf",fileId));
                outstream.write(response.returnContent().asBytes());
                log.info("调用成功！");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }catch (IOException e) {
                log.error(
                        "httpclient failure！request：{}； ", request, e);
            }
        });
    }
}