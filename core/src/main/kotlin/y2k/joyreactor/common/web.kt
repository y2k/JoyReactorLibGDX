package y2k.joyreactor.common

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
* Created by y2k on 03/12/2016.
**/

fun getDocumentAuthorized(
    readCookies: () -> Map<String, String>,
    writeCookies: (Map<String, String>) -> Unit,
    path: String): Document {

    return Jsoup
        .connect(makeUrl(path))
        .apply { cookies(readCookies()) }
        .userAgent("Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16")
        .execute()
        .apply { writeCookies(cookies()) }
        .parse()
}

fun postFormAuthorized(
    readCookies: () -> Map<String, String>,
    writeCookies: (Map<String, String>) -> Unit,
    path: String,
    form: Map<String, String>) {

    Jsoup
        .connect(makeUrl(path))
        .method(Connection.Method.POST)
        .apply { cookies(readCookies()) }
        .userAgent("Opera/9.80 (X11; Linux i686; Ubuntu/14.10) Presto/2.12.388 Version/12.16")
        .data(form)
        .execute()
        .apply { writeCookies(cookies()) }
}

private fun makeUrl(path: String) = "http://joyreactor.cc/" + path