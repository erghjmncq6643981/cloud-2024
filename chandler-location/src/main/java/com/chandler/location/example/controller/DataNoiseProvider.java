/*
 * chandler-location
 * 2024/4/8 10:01
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.controller;

import com.chandler.location.example.domain.dataobject.OriginalLocationData;
import com.chandler.location.example.entity.OriginalLocationParam;
import com.chandler.location.example.entity.dto.TruckDrivingResp;
import com.chandler.location.example.service.DataNoiseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/8 10:01
 * @since 1.8
 */
@RestController
@RequestMapping("noise")
public class DataNoiseProvider {
    @Autowired
    private DataNoiseHandler noiseHandler;

    @GetMapping(value = "/load/data")
    public void getStoppingLocations(@RequestParam String lpn,
                                     @RequestParam String sourceType,
                                     @RequestParam String filePath) {
        noiseHandler.readFile(lpn, sourceType, filePath);
    }

    @PostMapping(value = "/parse/data")
    public TruckDrivingResp parseLocationData(@RequestBody OriginalLocationParam param){
        return noiseHandler.parseLocationData(param);
    }
}