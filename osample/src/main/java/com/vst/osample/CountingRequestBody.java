package com.vst.osample;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/2/21
 * class description:请输入类描述
 */
public class CountingRequestBody extends RequestBody {
    protected RequestBody delegate;
    private Listener listener;
    private CountingSink countingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }

    public static interface Listener{
        void onRequestProgress(long byteWrited,long contentLength);
    }

    @Override
    public long contentLength()  {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        countingSink = new CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    protected final class CountingSink extends ForwardingSink{
        private long bytesWritten;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            listener.onRequestProgress(bytesWritten,contentLength());
        }
    }
}
