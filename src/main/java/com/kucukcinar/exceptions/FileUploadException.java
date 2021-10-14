package com.kucukcinar.exceptions;


import com.kucukcinar.responses.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * The type File upload exception.
 */
@ControllerAdvice
public class FileUploadException {

    /**
     * Handle max size exception response entity.
     *
     * @param e the e
     * @return the response entity
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException e){
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File is too big! Max file size : 5 MB."));
    }
}
