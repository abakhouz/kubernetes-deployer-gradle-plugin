package com.github.slamdev.kubernetes

import com.github.slamdev.kubernetes.internal.DeploySpec
import com.github.slamdev.kubernetes.internal.Deployer
import groovy.transform.CompileStatic
import org.gradle.api.internal.ConventionTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CompileStatic
@CacheableTask
@SuppressWarnings('GroovyUnusedDeclaration')
class DeployTask extends ConventionTask {

    private final Property<File> outputDir

    private final ListProperty<String> classifiers

    private final Property<String> dockerImageName

    private final Property<String> dockerImageRepository

    private final ListProperty<String> dockerImageTags

    private final Property<File> inheritFromDir

    private final Property<File> inputDir

    private final Property<Boolean> dryRun

    private final Property<Boolean> dockerOnly

    DeployTask() {
        outputDir = project.objects.property(File)
        classifiers = project.objects.listProperty(String)
        dockerImageName = project.objects.property(String)
        dockerImageRepository = project.objects.property(String)
        dockerImageTags = project.objects.listProperty(String)
        inheritFromDir = project.objects.property(File)
        inputDir = project.objects.property(File)
        dryRun = project.objects.property(Boolean)
        dockerOnly = project.objects.property(Boolean)
    }

    @TaskAction
    protected void deploy() {
        if (getInputDir() == null && getInheritFromDir() == null) {
            throw new TaskValidationException('Either inheritFromDir or inputDir should be specified', [])
        }
        DeploySpec spec = new DeploySpec(
                project: project,
                outputDir: getOutputDir().toPath(),
                classifiers: getClassifiers(),
                dockerImageName: getDockerImageName(),
                dockerImageRepository: getDockerImageRepository(),
                dockerImageTags: getDockerImageTags(),
                inheritFromDir: getInheritFromDir()?.toPath(),
                inputDir: getInputDir()?.toPath(),
                dryRun: isDryRun(),
                dockerOnly: isDockerOnly()
        )
        new Deployer(spec: spec).deploy()
    }

    @PathSensitive(RELATIVE)
    @OutputDirectory
    File getOutputDir() {
        outputDir.orNull
    }

    @Input
    List<String> getClassifiers() {
        classifiers.orNull
    }

    @Input
    String getDockerImageName() {
        dockerImageName.orNull
    }

    @Input
    String getDockerImageRepository() {
        dockerImageRepository.orNull
    }

    @Internal
    List<String> getDockerImageTags() {
        dockerImageTags.orNull
    }

    @PathSensitive(RELATIVE)
    @InputDirectory
    @Optional
    File getInheritFromDir() {
        inheritFromDir.orNull
    }

    @PathSensitive(RELATIVE)
    @InputDirectory
    @Optional
    File getInputDir() {
        inputDir.orNull
    }

    @Input
    Boolean isDryRun() {
        dryRun.orNull
    }

    @Input
    Boolean isDockerOnly() {
        dockerOnly.orNull
    }

    void setOutputDir(Property<File> outputDir) {
        this.outputDir.set(outputDir)
    }

    void setClassifiers(ListProperty<String> classifiers) {
        this.classifiers.set(classifiers)
    }

    void setDockerImageName(Property<String> dockerImageName) {
        this.dockerImageName.set(dockerImageName)
    }

    void setDockerImageRepository(Property<String> dockerImageRepository) {
        this.dockerImageRepository.set(dockerImageRepository)
    }

    void setDockerImageTags(ListProperty<String> dockerImageTags) {
        this.dockerImageTags.set(dockerImageTags)
    }

    void setInheritFromDir(Property<File> inheritFromDir) {
        this.inheritFromDir.set(inheritFromDir)
    }

    void setInputDir(Property<File> inputDir) {
        this.inputDir.set(inputDir)
    }

    void setDryRun(Property<Boolean> dryRun) {
        this.dryRun.set(dryRun)
    }

    void setDockerOnly(Property<Boolean> dockerOnly) {
        this.dockerOnly.set(dockerOnly)
    }
}
