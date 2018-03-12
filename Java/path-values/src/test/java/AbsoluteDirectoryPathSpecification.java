import autofixture.publicinterface.Any;
import logic.AbsoluteDirectoryPath;
import logic.AbsoluteFilePath;
import logic.DirectoryName;
import logic.FileName;
import lombok.val;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AbsoluteDirectoryPathSpecification {

    @DataProvider(name = "invalid values")
    public static Object[][] invalidValues() {
        return new Object[][]{
            {null, NullPointerException.class},
            {"", IllegalArgumentException.class},
            {"?", IllegalArgumentException.class},
            {"|", IllegalArgumentException.class},
            {"\"", IllegalArgumentException.class},
            {"\\\\\\\\\\\\\\\\\\\\/\\/", IllegalArgumentException.class}
        };
    }

    @Test(dataProvider = "invalid values")
    public void shouldThrowExceptionWhenCreatedWithInvalidValue(
        String invalidName,
        Class exceptionClass
    ) {
        assertThatThrownBy(() ->
            AbsoluteDirectoryPath.from(invalidName)
        ).isInstanceOf(exceptionClass);
    }

    @Test
    public void shouldCreatePathWithValueWhenInputIsValid() {
        String fromPath = "C:\\lolek";
        val path = AbsoluteDirectoryPath.from(fromPath);
        assertThat(path).isNotNull();
        assertThat(path.toString()).isEqualTo(fromPath);
    }

    @Test
    public void shouldAllowAppendingFileNameToCreateAbsoluteFilePath() {
        //GIVEN
        val path = Any.anonymous(AbsoluteDirectoryPath.class);
        val fileName = Any.instanceOf(FileName.class);
        AbsoluteFilePath absoluteFilePath = path.with(fileName);

        //WHEN
        val convertedToString = absoluteFilePath.toString();

        //THEN
        assertThat(Paths.get(path.toString(), fileName.toString()).toString())
            .isEqualTo(convertedToString);

    }

    @Test
    public void ShouldBeConvertibleToPath() {
        //GIVEN
        Path initialPath = Any.instanceOf(Path.class);
        val directoryPath = AbsoluteDirectoryPath.from(initialPath.toString());

        //WHEN
        Path javaPath = directoryPath.toJavaPath();

        //THEN
        assertThat(javaPath).isEqualTo(initialPath);
    }

    @Test
    public void ShouldAllowGettingPathRoot() {
        //GIVEN
        Path initialPath = Any.instanceOf(Path.class);
        val dir = AbsoluteDirectoryPath.from(
            initialPath.toString());

        //WHEN
        AbsoluteDirectoryPath root = dir.root();

        //THEN
        assertThat(initialPath.getRoot())
            .isEqualTo(root.toJavaPath());
    }

    @DataProvider(name = "pathsForSegmentCheck")
    public static Object[][] pathsForSegmentCheck() {
        return new Object[][]{
            {"C:\\parent\\child\\", "C:\\parent"},
            {"C:\\parent\\", "C:\\"},
        };
    }

    @Test(dataProvider = "pathsForSegmentCheck")
    public void ShouldAllowGettingProperParentDirectoryWhenItExists(String input, String expected) {
        //GIVEN
        val dir = AbsoluteDirectoryPath.from(input);

        //WHEN
        Optional<AbsoluteDirectoryPath> parent = dir.parent();

        //THEN
        assertThat(parent.isPresent()).isTrue();
        assertThat(parent.get().toString()).isEqualTo(expected);
    }

    @Test
    public void shouldProduceEmptyOptionalWhenAskedForParentThatDoesNotExist() {
        //GIVEN
        val rootString = Any.instanceOf(Path.class).getRoot().toString();
        val dir = AbsoluteDirectoryPath.from(rootString);

        //WHEN
        val parent = dir.parent();

        //THEN
        assertThat(parent).isEmpty();
    }

    @DataProvider(name = "pathsForCurrentDirRetrieval")
    public static Object[][] pathsForCurrentDirRetrieval() {
        return new Object[][]{
            {"C:\\parent\\child\\", "child"},
            {"C:\\parent\\", "parent"},
            {"C:\\", "C:\\"},
        };
    }

    @Test(dataProvider = "pathsForCurrentDirRetrieval")
    public void shouldAllowGettingTheNameOfCurrentDirectory(
        String fullPath, String expectedDirectoryName) {
        //GIVEN
        val directoryPath = AbsoluteDirectoryPath.from(fullPath);

        //WHEN
        DirectoryName dirName = directoryPath.directoryName();

        //THEN
        assertThat(dirName.toString()).isEqualTo(expectedDirectoryName);
    }

    @Test
    public void ShouldAllowAddingDirectoryName() {
        //GIVEN
        val directoryPath = AbsoluteDirectoryPath.from(
            "G:\\Directory\\Subdirectory");

        //WHEN
        AbsoluteDirectoryPath
            directoryPathWithAnotherDirectoryName =
            directoryPath.with(DirectoryName.from("Subdir2"));

        //THEN
        assertThat(directoryPathWithAnotherDirectoryName.toString())
            .isEqualTo("G:\\Directory\\Subdirectory\\Subdir2");

    }

    @Test
    public void ShouldAllowAddingDirectoryNameAndFileName() {
        //GIVEN
        val directoryPath = AbsoluteDirectoryPath.from("G:\\Directory\\Subdirectory");

        //WHEN
        val directoryName = DirectoryName.from("Lolek");
        val directoryName2 = DirectoryName.from("Lolek2");
        val fileName = FileName.from("File.txt");
        AbsoluteFilePath absoluteFilePath = directoryPath.with(directoryName).with(directoryName2).with(fileName);

        //THEN
        assertThat(absoluteFilePath.toString()).isEqualTo("G:\\Directory\\Subdirectory\\Lolek\\Lolek2\\File.txt");
    }



/*
    [Fact]
    public void ShouldAllowAddingRelativeDirectory()
    {
      //GIVEN
      var directoryPath = AbsoluteDirectoryPath.Value(@"G:\Directory\Subdirectory");

      //WHEN
      var relativePath = RelativeDirectoryPath.Value(@"Lolek\Lolek2");
      AbsoluteDirectoryPath newDirectoryPath = directoryPath + relativePath;

      //THEN
      Assert.Equal(@"G:\Directory\Subdirectory\Lolek\Lolek2", newDirectoryPath.ToString());
    }

    [Fact]
    public void ShouldAllowAddingRelativePathWithFileName()
    {
      //GIVEN
      var directoryPath = AbsoluteDirectoryPath.Value(@"G:\Directory\Subdirectory");

      //WHEN
      var relativePathWithFileName = RelativeFilePath.Value(@"Subdirectory2\file.txt");
      AbsoluteFilePath absoluteFilePath = directoryPath + relativePathWithFileName;

      //THEN
      Assert.Equal(@"G:\Directory\Subdirectory\Subdirectory2\file.txt", absoluteFilePath.ToString());
    }

    [Fact]
    public void ShouldBeConvertibleToAnyDirectoryPath()
    {
      //GIVEN
      var dirPath = Any.Instance<AbsoluteDirectoryPath>();

      //WHEN
      AnyDirectoryPath anyDirectoryPath = dirPath.AsAnyDirectoryPath();

      //THEN
      Assert.Equal(dirPath.ToString(), anyDirectoryPath.ToString());
    }

    [Fact]
    public void ShouldBeConvertibleToAnyPath()
    {
      //GIVEN
      var directorypath = Any.Instance<AbsoluteDirectoryPath>();

      //WHEN
      AnyPath anyPathWithFileName = directorypath.AsAnyPath();

      //THEN
      Assert.Equal(directorypath.ToString(), anyPathWithFileName.ToString());
    }

    [Fact]
    public void ShouldDetermineEqualityToAnotherInstanceUsingFileSystemComparisonRules()
    {
      //GIVEN
      var directoryPath1 = Any.Instance<AbsoluteDirectoryPath>();
      var directoryPath2 = Any.Instance<AbsoluteDirectoryPath>();
      var fileSystemComparisonRules = Substitute.For<FileSystemComparisonRules>();
      var comparisonResult = Any.Boolean();

      fileSystemComparisonRules
        .ArePathStringsEqual(directoryPath1.ToString(), directoryPath2.ToString())
        .Returns(comparisonResult);


      //WHEN
      var equality = directoryPath1.ShallowEquals(directoryPath2, fileSystemComparisonRules);

      //THEN
      XAssert.Equal(comparisonResult, equality);
    }

     */
}

