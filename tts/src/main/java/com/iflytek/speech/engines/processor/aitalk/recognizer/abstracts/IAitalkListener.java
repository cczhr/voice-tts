package com.iflytek.speech.engines.processor.aitalk.recognizer.abstracts;

import java.util.List;

public interface IAitalkListener {
    void onError(int i);

    void onResults(List<AitalkContent> list);
}
