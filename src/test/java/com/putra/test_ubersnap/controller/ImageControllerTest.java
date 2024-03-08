package com.putra.test_ubersnap.controller;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.putra.test_ubersnap.constant.ImageConstant;
import com.putra.test_ubersnap.response.ValidatorResponse;

@SpringBootTest
class ImageControllerTest {

        private MockMvc mockMvc;

        @Autowired
        private ImageController imageController;


        @BeforeEach
        void setup() {
                this.mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        }

        private byte[] readImageAsBytes(String filename) throws IOException {
                Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
                Path joinedPath = Paths.get(path.toString(),
                                "src\\test\\java\\com\\putra\\test_ubersnap\\controller\\images",
                                filename);
                return Files.readAllBytes(joinedPath);
        }


        /// ---------------------------
        /// START
        ///
        /// CONVERT ENDPOINT
        /// ---------------------------
        @Test
        void whenCallConvertAPI_shoulReturnImageParamIsRequired() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/convert"))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content()
                                                .string(ImageConstant.ERROR_IMAGE_PARAM));
        }

        @Test
        void whenCallConvertAPI_ifImageIsNotPNG_shouldReturnErrorTypeImageIsNotSupported()
                        throws Exception {
                byte[] contents = readImageAsBytes("dj.png");
                MockMultipartFile mockImage = new MockMultipartFile("image", "test.gif",
                                MediaType.IMAGE_GIF_VALUE, contents);
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/convert")
                                .file(mockImage)).andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content()
                                                .string("Only PNG is supported"));
        }

        @Test
        void whenCallConvertAPI_shoulReturnConvertedImageAsBytes() throws Exception {
                byte[] results = readImageAsBytes("test.jpeg");
                MockMultipartFile mockImage = new MockMultipartFile("image", "test.png",
                                MediaType.IMAGE_PNG_VALUE, readImageAsBytes("dj.png"));
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/convert")
                                .file(mockImage)).andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().bytes(results));
        }
        /// ---------------------------
        /// END
        /// ---------------------------


        /// ---------------------------
        /// START
        ///
        /// RESIZE ENDPOINT
        /// ---------------------------
        @Test
        void whenCallResizeAPI_shoulReturnAllParamRequired() throws Exception {
                List<ValidatorResponse> contentResponse = new ArrayList<>();
                contentResponse.add(
                                new ValidatorResponse("width", "'width' paramater is required"));
                contentResponse.add(
                                new ValidatorResponse("height", "'height' paramater is required"));
                contentResponse.add(
                                new ValidatorResponse("image", "'image' paramater is required"));
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/resize"))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper()
                                                .writeValueAsString(contentResponse)));
        }

        @Test
        void whenCallResizeAPI_ifAllParamExistAndTrue_shouldReturnImageAsBytes() throws Exception {
                byte[] contents = readImageAsBytes("test.jpeg");
                byte[] actualContents = readImageAsBytes("test_resize_100x100.jpeg");
                MockMultipartFile mockImage = new MockMultipartFile("image", "test.jpeg",
                                MimeTypeUtils.IMAGE_JPEG_VALUE, contents);
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("width", "100");
                params.add("height", "100");
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/resize")
                                .file(mockImage).params(params))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().bytes(actualContents));;
        }

        @Test
        void whenCallResizeAPI_ifAllParamExistAndFileIsNotImage_shouldReturnErrorMessage()
                        throws Exception {
                byte[] contents = readImageAsBytes("test.jpeg");
                MockMultipartFile mockImage = new MockMultipartFile("image", "test.pdf",
                                MimeTypeUtils.TEXT_PLAIN_VALUE, contents);
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("width", "100");
                params.add("height", "100");
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/resize")
                                .file(mockImage).params(params))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content()
                                                .string(ImageConstant.ERROR_IMAGE_NOT_SUPPORTED));
        }
        /// ---------------------------
        /// END
        /// ---------------------------

        /// ---------------------------
        /// START
        ///
        /// CONVERT ENDPOINT
        /// ---------------------------
        @Test
        void whenCallCompressAPI_shoulReturnImageParamIsRequired() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/compress"))
                                .andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content()
                                                .string(ImageConstant.ERROR_IMAGE_PARAM));
        }

        @Test
        void whenCallCompressAPI_ifFileIsNotValidImage_shouldReturnErrorTypeImageIsNotSupported()
                        throws Exception {
                byte[] contents = readImageAsBytes("dj.png");
                MockMultipartFile mockImage = new MockMultipartFile("image", "test.txt",
                                MediaType.TEXT_PLAIN_VALUE, contents);
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/compress")
                                .file(mockImage)).andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                                .andExpect(MockMvcResultMatchers.content()
                                                .string(ImageConstant.ERROR_IMAGE_NOT_SUPPORTED));
        }

        @Test
        void whenCallCompressAPI_ifFileIsValidImage_shouldReturnImageAsBytes() throws Exception {
                byte[] contents = readImageAsBytes("dj.png");
                byte[] actualContents = readImageAsBytes("dj_compress.png");
                MockMultipartFile mockImage = new MockMultipartFile("image", "dj.png",
                                MediaType.IMAGE_PNG_VALUE, contents);
                this.mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/compress")
                                .file(mockImage)).andDo(MockMvcResultHandlers.print())
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.content().bytes(actualContents));
        }


        /// ---------------------------
        /// END
        /// ---------------------------


}
