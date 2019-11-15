/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package me.porcelli.nio.jgit.impl.daemon.git;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.internal.storage.reftree.RefTreeDatabase;
import org.eclipse.jgit.lib.BatchRefUpdate;
import org.eclipse.jgit.lib.NullProgressMonitor;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;

public class KetchCustomReceivePack extends ReceivePack {

    public KetchCustomReceivePack(final Repository into) {
        super(into);
    }

    @Override
    public void setAdvertisedRefs(final Map<String, Ref> allRefs,
                                  final Set<ObjectId> additionalHaves) {
        super.setAdvertisedRefs(allRefs,
                                additionalHaves);
        final Map<String, Ref> refs = getAdvertisedRefs();
        if (getRepository().getRefDatabase() instanceof RefTreeDatabase) {
            final RefDatabase bootstrap = ((RefTreeDatabase) getRepository().getRefDatabase()).getBootstrap();
            try {
                for (Map.Entry<String, Ref> entry : bootstrap.getRefs("").entrySet()) {
                    refs.put(entry.getKey(),
                             entry.getValue());
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void executeCommands() {
        if (getRepository().getRefDatabase() instanceof RefTreeDatabase) {
            List<ReceiveCommand> toApply = filterCommands(ReceiveCommand.Result.NOT_ATTEMPTED);
            if (toApply.isEmpty()) {
                return;
            }
            final BatchRefUpdate batch = ((RefTreeDatabase) getRepository().getRefDatabase()).getBootstrap().newBatchUpdate();
            batch.setAllowNonFastForwards(true);
            batch.setAtomic(false);
            batch.setRefLogIdent(getRefLogIdent());
            batch.setRefLogMessage("push",
                                   true); //$NON-NLS-1$
            batch.addCommand(toApply);
            try {
                batch.setPushCertificate(getPushCertificate());
                batch.execute(getRevWalk(),
                              NullProgressMonitor.INSTANCE);
            } catch (IOException err) {
                for (ReceiveCommand cmd : toApply) {
                    if (cmd.getResult() == ReceiveCommand.Result.NOT_ATTEMPTED) {
                        cmd.setResult(ReceiveCommand.Result.REJECTED_OTHER_REASON,
                                      MessageFormat.format(
                                              JGitText.get().lockError,
                                              err.getMessage()));
                    }
                }
            }
        } else {
            super.executeCommands();
        }
    }
}
