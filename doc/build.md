### ClaymoreDependencyPlugin

The plugin applies claymore annotations dependency and processors either:
    - from remote (maven central) if the gradle property `fetchRemoteDependency` is provided;
    - from local (`:annotations`, `:processors`) otherwise.