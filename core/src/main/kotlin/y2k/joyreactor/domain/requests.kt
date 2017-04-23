//package y2k.joyreactor.domain
//
//import y2k.joyreactor.common.Environment
//import y2k.joyreactor.common.doAsync
//
///**
// * Created by y2k on 29/11/2016.
// */
//fun requestProfile(env: Environment) = doAsync {
//    env.downloadDocument("user/_y2k").let(::parseProfile)
//}
//
//fun requestTags(env: Environment) = doAsync {
//    env.downloadDocument("user/_y2k").let(::parseTags)
//}