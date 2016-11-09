﻿using System.Collections.Generic;

namespace CommandQuerySeparation._01_ReturningPlainTypes
{
  public class MyDeletedFiles : DeletedFiles
  {
    private readonly List<string> _deletedFilesList;

    public MyDeletedFiles(List<string> deletedFilesList)
    {
      _deletedFilesList = deletedFilesList;
    }

    public void WriteOn(FileListDestination destination)
    {
      
    }
  }
}