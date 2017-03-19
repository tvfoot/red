package io.oldering.tvfoot.red.matches;

interface MatchesIntent {
  final class LoadFirstPage implements MatchesIntent {
  }

  final class LoadNextPage implements MatchesIntent {
    private final int currentPage;

    public LoadNextPage(int currentPage) {
      this.currentPage = currentPage;
    }

    public int currentPage() {
      return currentPage;
    }
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
