package io.oldering.tvfoot.red.matches;

interface MatchesIntent {
  final class LoadFirstPage implements MatchesIntent {
  }

  final class LoadNextPage implements MatchesIntent {
  }

  final class MatchRowClick implements MatchesIntent {
    private final MatchRowDisplayable match;

    MatchRowClick(MatchRowDisplayable match) {
      this.match = match;
    }

    public MatchRowDisplayable getMatch() {
      return match;
    }
  }
}
