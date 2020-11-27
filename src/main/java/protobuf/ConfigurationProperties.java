package protobuf;

public class ConfigurationProperties {
    String fileSystemPath;
    String generatePackagePath;
    String[] protobufBeanPackagePaths;

    public String getFileSystemPath() {
        return fileSystemPath;
    }

    public void setFileSystemPath(String fileSystemPath) {
        this.fileSystemPath = fileSystemPath;
    }

    public String getGeneratePackagePath() {
        return generatePackagePath;
    }

    public void setGeneratePackagePath(String generatePackagePath) {
        this.generatePackagePath = generatePackagePath;
    }

    public String[] getProtobufBeanPackagePaths() {
        return protobufBeanPackagePaths;
    }

    public void setProtobufBeanPackagePaths(String[] protobufBeanPackagePaths) {
        this.protobufBeanPackagePaths = protobufBeanPackagePaths;
    }
}
