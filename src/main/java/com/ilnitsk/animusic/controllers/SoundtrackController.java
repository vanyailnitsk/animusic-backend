package com.ilnitsk.animusic.controllers;

import com.ilnitsk.animusic.dto.SoundtrackRequest;
import com.ilnitsk.animusic.models.Soundtrack;
import com.ilnitsk.animusic.services.SoundtrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/soundtracks")
@Slf4j
public class SoundtrackController {
    private final SoundtrackService soundtrackService;

    @Autowired
    public SoundtrackController(SoundtrackService soundtrackService) {
        this.soundtrackService = soundtrackService;
    }

    @GetMapping("/play/{trackId}")
    public ResponseEntity<StreamingResponseBody> playTrack(
            @PathVariable Integer trackId,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range) throws IOException {
        List<HttpRange> httpRangeList = HttpRange.parseRanges(range);
        log.info("Playing track: {}", trackId);
        return soundtrackService.getAudioStream(trackId, httpRangeList.size() > 0 ? httpRangeList.get(0) : null);
    }

    @PostMapping("/create-from-file")
    public Soundtrack createFromFile(@RequestPart(value = "file", required = true) MultipartFile file,
                                     @ModelAttribute SoundtrackRequest request) {
        return soundtrackService.createSoundtrack(
                file, request.createSoundtrack(), request.getPlaylistId()
        );
    }

    @PostMapping("/create-from-youtube")
    public Soundtrack createFromYoutube(@RequestBody(required = true) SoundtrackRequest request) {
        return soundtrackService.createSoundtrack(
                request.getVideoUrl(), request.createSoundtrack(), request.getPlaylistId()
        );
    }

    @DeleteMapping("{id}")
    public void deleteSoundtrack(@PathVariable Integer id) {
        soundtrackService.remove(id);
    }
}