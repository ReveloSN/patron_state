import type { Reaction } from "../types/reaction";
import { getStatusMeta } from "../utils/phaseLabels";

interface PhaseStatusPanelProps {
  reaction: Reaction;
}

export function PhaseStatusPanel({ reaction }: PhaseStatusPanelProps) {
  const statusMeta = getStatusMeta(reaction.status);

  return (
    <section className="status-panel">
      <div className="status-panel__headline">
        <span className="status-chip">{statusMeta.label}</span>
        <h2>{reaction.name}</h2>
        <p>{reaction.message}</p>
        <span className="id-line">Reaction ID: {reaction.id}</span>
      </div>

      <div className="status-visual">
        <div className="status-visual__ring" />
        <div className="status-visual__core" />
        <div className="status-visual__label">
          <strong>{statusMeta.label}</strong>
          <span>{statusMeta.detail}</span>
        </div>
      </div>

      <div className="status-metrics">
        <div className="metric">
          <span>Registered reactants</span>
          <strong>{reaction.reactants.length}</strong>
        </div>
        <div className="metric">
          <span>Recorded measurements</span>
          <strong>{reaction.measurements.length}</strong>
        </div>
      </div>
    </section>
  );
}
