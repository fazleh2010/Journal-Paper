;;; fill-grammar-frame.el --- fill grammar frame -*- lexical-binding: t; -*-

;; Copyright (C) 2021  Andreas Röhler <andreas.roehler@online.de>

;; Author: Andreas Röhler
;; Keywords: convenience

;; This program is free software; you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <https://www.gnu.org/licenses/>.

;;; Commentary:

;;

;;; Code:

;;

;; configure the sources
;; (defvar fill-grammar-frame-file PATH/TO/grammar-frame.csv)
;; (defvar english-verbs-file PATH/TO/en-verbs.txt)

(defun fill-grammar-frame ()
  "Create missing entries in grammar fram from english verb resource."
  (interactive "*")
  (let ((grammar-buffer (buffer-name (find-file grammar-frame-file)))
	verbs-buffer)
    (goto-char (point-min))
    (setq verbs-buffer (find-file-noselect english-verbs-file))
    (while (re-search-forward "^\\([^,]+,[^,]+,\\)\\([^,]+\\)," nil t 1)
      (let ((erg (match-string-no-properties 2))
	    writtenForm3rdPerson
	    writtenFormPast)
	(with-current-buffer verbs-buffer
	  (switch-to-buffer (current-buffer))
	  (goto-char (point-min))
	  (when (re-search-forward (concat "^" erg ",") nil t 1 )
	    ;; flow,,,flows,,flowing,,,,,flowed,flowed,,,,,,,,,,,,
	    (setq writtenForm3rdPerson (and
					(looking-at ",,\\([^,]+\\),")
					;; \\(,[^,]*,[^,]+,\\)\\([^,]+\\)")
					(match-string-no-properties 1)
					))
	    (setq writtenFormPast (and
				   (looking-at ",,\\([^,]+\\),,[^,]+,,,,,\\([^,]+\\),")
				   ;; \\(,[^,]*,[^,]+,\\)\\([^,]+\\)")
				   (match-string-no-properties 2)
				   ))
	    (message "%s" writtenForm3rdPerson)
	    ))
	(set-buffer grammar-buffer)
	(switch-to-buffer (current-buffer))
	(when (and writtenForm3rdPerson
		   (looking-at  ",,"))
	  (insert writtenForm3rdPerson)
	  (forward-char 1)
	  (when writtenFormPast
	    (insert writtenFormPast))))
      )))

(provide 'fill-grammar-frame)
;;; fill-grammar-frame.el ends here
