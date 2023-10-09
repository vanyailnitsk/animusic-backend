package com.ilnitsk.animusic.controllers;

import com.ilnitsk.animusic.services.SoundtrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/soundtracks")
public class SountrackController {
    private final SoundtrackService soundtrackService;

    @Autowired
    public SountrackController(SoundtrackService soundtrackService) {
        this.soundtrackService = soundtrackService;
    }

    @GetMapping("/play/{trackId}")
    public ResponseEntity<StreamingResponseBody> playTrack(
            @PathVariable Integer trackId,
            @RequestHeader(value = HttpHeaders.RANGE,required = false) String range) throws IOException {
        List<HttpRange> httpRangeList = HttpRange.parseRanges(range);
        System.out.println(httpRangeList);
        return soundtrackService.getAudioStream(trackId, httpRangeList.size() > 0 ? httpRangeList.get(0) : null);
    }


}
