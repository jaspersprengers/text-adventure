module secretkey {
    requires adventure.api;
    provides adventure.api.Story with epics.secretkey.SecretKey;
}
