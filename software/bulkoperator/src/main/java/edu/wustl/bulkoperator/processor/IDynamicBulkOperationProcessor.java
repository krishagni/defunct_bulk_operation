/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package edu.wustl.bulkoperator.processor;


import edu.wustl.bulkoperator.csv.CsvReader;
import edu.wustl.bulkoperator.csv.impl.CsvFileReader;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.common.beans.SessionDataBean;

public interface IDynamicBulkOperationProcessor
{

	Object process(CsvReader csvReader, int csvRowCounter,
			SessionDataBean sessionDataBean) throws BulkOperationException,
			Exception;
}
