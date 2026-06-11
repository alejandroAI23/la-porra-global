import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { api } from '../api/client.js'

export default function LeagueRanking() {
  const { id } = useParams()
  const [league, setLeague] = useState(null)
  const [ranking, setRanking] = useState([])
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function load() {
      setLoading(true)
      setError(null)
      try {
        const [lg, rk] = await Promise.all([
          api.getLeague(id),
          api.getLeagueRanking(id),
        ])
        setLeague(lg)
        setRanking(rk)
      } catch (e) {
        setError(e.message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [id])

  return (
    <section>
      <Link to="/ligas" className="text-sm text-slate-400 hover:text-pitch-500">
        ← Volver a ligas
      </Link>
      <h1 className="mt-2 text-2xl font-bold">
        {league ? league.name : 'Ranking'}
      </h1>
      {league && (
        <p className="mt-1 text-sm text-slate-400">
          {league.memberCount} miembros · Código:{' '}
          <span className="font-mono text-pitch-400">{league.inviteCode}</span>
        </p>
      )}

      {loading && <p className="mt-6 text-slate-400">Cargando…</p>}
      {error && <p className="mt-6 text-red-400">{error}</p>}

      {!loading && !error && (
        <div className="mt-6 overflow-hidden rounded-xl border border-slate-700">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-800 text-slate-400">
              <tr>
                <th className="px-4 py-3">#</th>
                <th className="px-4 py-3">Jugador</th>
                <th className="px-4 py-3 text-right">Puntos</th>
                <th className="px-4 py-3 text-right">Exactos</th>
                <th className="px-4 py-3 text-right">Aciertos</th>
              </tr>
            </thead>
            <tbody>
              {ranking.length === 0 && (
                <tr>
                  <td colSpan="5" className="px-4 py-6 text-center text-slate-500">
                    Aún no hay puntos. ¡Empieza a predecir!
                  </td>
                </tr>
              )}
              {ranking.map((row) => (
                <tr key={row.userId} className="border-t border-slate-800">
                  <td className="px-4 py-3 font-semibold text-pitch-500">
                    {row.position}
                  </td>
                  <td className="px-4 py-3">{row.displayName ?? row.username}</td>
                  <td className="px-4 py-3 text-right font-semibold">
                    {row.totalPoints}
                  </td>
                  <td className="px-4 py-3 text-right text-slate-400">
                    {row.exactHits}
                  </td>
                  <td className="px-4 py-3 text-right text-slate-400">
                    {row.outcomeHits}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  )
}
