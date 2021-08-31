package br.com.cecafes.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin
@RequestMapping("/arquivos")
public class ArquivosController {

    @Value("${arquivos.cecafes}")
    private String localDasImagens;

    @RequestMapping(value = "/fotos/{fileName}", method = RequestMethod.GET, produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public byte[] getFile(@PathVariable("fileName") String fileName) throws IOException {
        FileSystemResource fs = new FileSystemResource(localDasImagens+fileName);
        InputStream in = fs.getInputStream();
        return in.readAllBytes();
    }

}
