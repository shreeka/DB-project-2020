package com.example.GrpSB.repo;
import com.example.GrpSB.model.MoviePlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PlotSummaryRepository extends JpaRepository<MoviePlot, String>  {

    @Query(nativeQuery = true, value ="select q.movie_title, m.title_type, m.title_name, q.plot_summary, q.plot_synopsis, q.rank\n" +
            "from movie_titles m inner join\n" +
            "(SELECT movie_title,plot_summary,plot_synopsis, ts_rank_cd(v, ts_q) as rank FROM movie_plots m,\n" +
            "to_tsvector(m.plot_summary) v,\n" +
            "to_tsquery(:plot_summary) ts_q\n" +
            "WHERE to_tsvector(m.plot_summary) @@ to_tsquery(:plot_summary)) q\n" +
            "on m.id = q.movie_title\n" +
            "order by rank desc\n" +
            "limit 3")
    List<MoviePlot> findByMoviePlotSummary(@Param("plot_summary") String plot_summary);
}
